package nl.entreco.dartsscorecard.play

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.view.Menu
import android.view.MenuItem
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.ad.AdProvider
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.Logger
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.*
import nl.entreco.domain.play.mastercaller.MasterCaller
import nl.entreco.domain.play.mastercaller.MasterCallerRequest
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.play.revanche.RevancheRequest
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.MarkGameAsFinishedRequest
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.play.stats.StoreTurnRequest
import nl.entreco.domain.play.stats.UndoTurnRequest
import nl.entreco.domain.play.stats.UndoTurnResponse
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(private val playGameUsecase: Play01Usecase,
                                          private val revancheUsecase: RevancheUsecase,
                                          private val gameListeners: Play01Listeners,
                                          private val masterCaller: MasterCaller,
                                          private val dialogHelper: DialogHelper,
                                          private val toggleSoundUsecase: ToggleSoundUsecase,
                                          private val audioPrefRepository: AudioPrefRepository,
                                          private val adProvider: AdProvider,
                                          private val logger: Logger) : BaseViewModel(), UiCallback, InputListener {

    val loading = ObservableBoolean(true)
    val finished = ObservableBoolean(false)
    val errorMsg = ObservableInt()
    private lateinit var game: Game
    private lateinit var request: Play01Request
    private lateinit var teams: Array<Team>
    private lateinit var load: GameLoadedNotifier<ScoreSettings>
    private lateinit var loaders: Array<GameLoadedNotifier<Play01Response>>

    fun load(request: Play01Request, load: GameLoadedNotifier<ScoreSettings>, vararg loaders: GameLoadedNotifier<Play01Response>) {
        this.request = request
        this.load = load
        this.loaders = arrayOf(*loaders)
        this.playGameUsecase.loadGameAndStart(request,
                { response ->
                    this.game = response.game
                    this.teams = response.teams
                    this.load.onLoaded(response.teams, game.scores, response.settings, this)
                    this.loaders.forEach {
                        it.onLoaded(response.teams, game.scores, response, null)
                    }
                },
                { err ->
                    logger.e("err: $err")
                    loading.set(false)
                    errorMsg.set(R.string.err_unable_to_retrieve_game)
                })
    }

    override fun onRevanche() {
        dialogHelper.revanche(request.startIndex, teams) { startIndex ->
            val nextTeam = (startIndex) % teams.size
            revancheUsecase.recreateGameAndStart(RevancheRequest(request, teams, nextTeam),
                    { response ->
                        this.request = this.request.copy(gameId = response.game.id, startIndex = nextTeam)
                        this.game = response.game
                        this.teams = response.teams
                        this.load.onLoaded(response.teams, game.scores, response.settings, this)
                        this.loaders.forEach {
                            val playResponse = Play01Response(response.game, response.settings, response.teams, response.teamIds)
                            it.onLoaded(response.teams, game.scores, playResponse, null)
                        }
                    },
                    { err ->
                        logger.e("err: $err")
                        loading.set(false)
                        errorMsg.set(R.string.err_unable_to_revanche)
                    })
        }
    }

    fun registerListeners(scoreListener: ScoreListener, statListener: StatListener, specialEventListener: SpecialEventListener<*>, vararg playerListeners: PlayerListener) {
        gameListeners.registerListeners(scoreListener, statListener, specialEventListener, *playerListeners)
    }

    override fun onLetsPlayDarts(listeners: List<TeamScoreListener>) {
        this.gameListeners.onLetsPlayDarts(game, listeners)
        this.loading.set(false)
        this.finished.set(false)
    }

    override fun onUndo() {
        loading.set(true)
        playGameUsecase.undoLastTurn(UndoTurnRequest(game.id), undoSuccess(), undoFailed())
    }

    private fun undoFailed(): (Throwable) -> Unit {
        return { err ->
            logger.w("Undo failed! -> $err")
        }
    }

    private fun undoSuccess(): (UndoTurnResponse) -> Unit {
        return {
            logger.i("Undo done! -> go to Let's Play Darts Function")
            load(request, load, *loaders)
        }
    }

    override fun onDartThrown(turn: Turn, by: Player) {
        this.gameListeners.onDartThrown(turn, by)
    }

    override fun onTurnSubmitted(turn: Turn, by: Player) {
        handleTurn(turn, by)
    }

    private fun handleTurn(turn: Turn, by: Player) {
        game.handle(turn)

        val next = game.next
        val scores = game.scores

        handleGameFinished(next, game.id)
        notifyListeners(next, turn, by, scores)
        notifyMasterCaller(next, turn)
        showInterstitial(next)
        storeTurn(turn, by, next)
    }

    private fun storeTurn(turn: Turn, by: Player, next: Next) {
        val turnRequest = StoreTurnRequest(by.id, game.id, turn, next.state)
        val score = game.previousScore()
        val started = game.isNewMatchLegOrSet()
        val turnCounter = game.getTurnCount()
        val breakMade = game.wasBreakMade(by)
        val turnMeta = TurnMeta(by.id, turnCounter, score, started, breakMade)
        playGameUsecase.storeTurnAndMeta(turnRequest, turnMeta, { turnId, metaId ->
            gameListeners.onStatsUpdated(turnId, metaId)
        })
    }

    private fun handleGameFinished(next: Next, gameId: Long) {
        val gameFinished = next.state == State.MATCH
        finished.set(gameFinished)
        if (gameFinished) {
            playGameUsecase.markGameAsFinished(MarkGameAsFinishedRequest(gameId))
            gameListeners.onGameFinished(gameId)
        }
    }

    private fun notifyListeners(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        gameListeners.onTurnSubmitted(next, turn, by, scores)
    }

    private fun notifyMasterCaller(next: Next, turn: Turn) {
        when (next.state) {
            State.START -> masterCaller.play(MasterCallerRequest(start = true))
            State.LEG -> masterCaller.play(MasterCallerRequest(leg = true))
            State.SET -> masterCaller.play(MasterCallerRequest(set = true))
            State.MATCH -> masterCaller.play(MasterCallerRequest(match = true))
            State.ERR_BUST -> masterCaller.play(MasterCallerRequest(0))
            else -> masterCaller.play(MasterCallerRequest(turn.total()))
        }
    }

    private fun showInterstitial(next: Next) {
        when (next.state) {
            State.START -> adProvider.provideInterstitial()
            State.LEG -> adProvider.provideInterstitial()
            State.SET -> adProvider.provideInterstitial()
            State.MATCH -> adProvider.provideInterstitial()
            else -> {
            }
        }
    }

    fun stop() {
        masterCaller.stop()
    }

    fun initToggleMenuItem(menu: Menu?) {
        menu?.findItem(R.id.menu_sound_settings)?.isChecked = audioPrefRepository.isMasterCallerEnabled()
    }

    fun toggleMasterCaller(item: MenuItem) {
        item.isChecked = !item.isChecked
        toggleSoundUsecase.toggle()
    }
}
