package nl.entreco.dartsscorecard.play

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.ad.AdViewModel
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.*
import nl.entreco.domain.play.mastercaller.MasterCaller
import nl.entreco.domain.play.mastercaller.MasterCallerRequest
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.play.revanche.RevancheRequest
import nl.entreco.domain.play.revanche.RevancheResponse
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.*
import nl.entreco.domain.play.stats.StoreTurnRequest
import nl.entreco.domain.play.stats.UndoTurnRequest
import nl.entreco.domain.play.stats.UndoTurnResponse
import nl.entreco.domain.rating.AskForRatingResponse
import nl.entreco.domain.rating.AskForRatingUsecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.settings.ScoreSettings
import nl.entreco.shared.log.Logger
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(private val playGameUsecase: Play01Usecase,
                                          private val revancheUsecase: RevancheUsecase,
                                          private val gameListeners: Play01Listeners,
                                          private val masterCaller: MasterCaller,
                                          @ActivityScope private val dialogHelper: DialogHelper,
                                          private val toggleSoundUsecase: ToggleSoundUsecase,
                                          private val askForRatingUsecase: AskForRatingUsecase,
                                          private val audioPrefRepository: AudioPrefRepository,
                                          private val adViewModel: AdViewModel,
                                          @ActivityScope private val logger: Logger) :
        BaseViewModel(), UiCallback, InputListener {

    val loading = ObservableBoolean(true)
    val finished = ObservableBoolean(false)
    val errorMsg = ObservableInt()
    private lateinit var game: Game
    private lateinit var request: Play01Request
    private lateinit var teams: Array<Team>
    private var load: GameLoadedNotifier<ScoreSettings>? = null
    private var loaders: Array<GameLoadedNotifier<Play01Response>>? = null

    fun load(request: Play01Request, load: GameLoadedNotifier<ScoreSettings>,
             vararg loaders: GameLoadedNotifier<Play01Response>) {
        this.load = load
        this.loaders = arrayOf(*loaders)
        this.playGameUsecase.loadGameAndStart(request,
                { response ->
                    onGameOk(request, response, null)
                },
                onGameFailed(R.string.err_unable_to_retrieve_game))
    }

    override fun onRevanche() {
        dialogHelper.revanche(request.startIndex, teams) { startIndex ->
            val nextTeam = (startIndex) % teams.size
            revancheUsecase.recreateGameAndStart(RevancheRequest(request, teams, nextTeam),
                    { revenge ->
                        onGameOk(this.request.copy(gameId = revenge.game.id, startIndex = nextTeam),
                                null, revenge)
                    },
                    onGameFailed(R.string.err_unable_to_revanche))
        }
    }

    private fun onGameOk(request: Play01Request, response: Play01Response?,
                         revancheResponse: RevancheResponse?) {
        this.request = request
        this.game = response?.game ?: revancheResponse!!.game
        this.teams = response?.teams ?: revancheResponse!!.teams
        val teamIds = response?.teamIds ?: revancheResponse!!.teamIds
        val settings = response?.settings ?: revancheResponse!!.settings

        if(this.game.next.state == State.START){
            this.masterCaller.play(MasterCallerRequest(start = true))
        }

        this.load?.onLoaded(teams, game.scores, settings, this)
        this.loaders?.forEach {
            it.onLoaded(teams, game.scores,
                    response ?: Play01Response(game, settings, teams, teamIds), null)
        }
    }

    private fun onGameFailed(@StringRes error: Int): (Throwable) -> Unit {
        return { err ->
            logger.e("err: $err")
            loading.set(false)
            errorMsg.set(error)
        }
    }

    fun registerListeners(scoreListener: ScoreListener, statListener: StatListener,
                          specialEventListener: SpecialEventListener<*>,
                          vararg playerListeners: PlayerListener) {
        gameListeners.registerListeners(scoreListener, statListener, specialEventListener,
                *playerListeners)
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
            load(request, load!!, *loaders!!)
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

        handleGameFinished(next, game.id, by.id)
        notifyListeners(next, turn, by, scores)
        notifyMasterCaller(next, turn)
        showInterstitial(next)
        storeTurn(turn, by, next)
    }

    private fun storeTurn(turn: Turn, by: Player, next: Next) {
        val turnRequest = StoreTurnRequest(by.id, game.id, turn, next.state)
        val score = game.previousScore()
//        val started = game.isNewMatchLegOrSet()
        val turnCounter = game.getTurnCount()
        val breakMade = game.wasBreakMade(by)
        val turnMeta = TurnMeta(by.id, turnCounter, score, breakMade)
        playGameUsecase.storeTurnAndMeta(turnRequest, turnMeta) { turnId, metaId ->
            gameListeners.onStatsUpdated(turnId, metaId)
        }
    }

    private fun handleGameFinished(next: Next, gameId: Long, winnerId: Long) {
        val gameFinished = next.state == State.MATCH
        finished.set(gameFinished)
        if (gameFinished) {
            askForRatingUsecase.go(onShouldAskForRating(), {})
            playGameUsecase.markGameAsFinished(MarkGameAsFinishedRequest(gameId,
                    teams.first { it.contains(winnerId) }.toTeamString()))
            gameListeners.onGameFinished(gameId)
        }
    }

    private fun onShouldAskForRating(): (AskForRatingResponse) -> Unit = { response ->
        if (response.shouldAskForRating) {
            dialogHelper.showRatingDialog()
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
            State.START -> adViewModel.provideInterstitial()
            State.LEG -> adViewModel.provideInterstitial()
            State.SET -> adViewModel.provideInterstitial()
            State.MATCH -> adViewModel.provideInterstitial()
            else -> { /* ignore */
            }
        }
    }

    fun stop() {
        masterCaller.stop()
    }

    fun initToggleMenuItem(menu: Menu?) {
        menu?.findItem(R.id.menu_sound_settings)
                ?.isChecked = audioPrefRepository.isMasterCallerEnabled()
    }

    fun toggleMasterCaller(item: MenuItem) {
        item.isChecked = !item.isChecked
        toggleSoundUsecase.toggle()
    }

    fun askToDeleteMatch(onConfirm: ()->Unit){
        dialogHelper.showConfirmDeleteDialog{
            loading.set(true)
            playGameUsecase.deleteMatch(DeleteGameRequest(game.id)) {
                loading.set(false)
                onConfirm()
            }
        }
    }

    override fun onCleared() {
        load = null
        loaders = null
        super.onCleared()
    }
}
