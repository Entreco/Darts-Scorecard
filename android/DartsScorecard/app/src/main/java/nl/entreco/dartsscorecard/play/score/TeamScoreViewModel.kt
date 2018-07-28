package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.finish.GetFinishRequest
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.domain.play.listeners.events.NineDartEvent
import java.util.concurrent.Future

/**
 * Created by Entreco on 22/11/2017.
 */
class TeamScoreViewModel(val team: Team, startScore: Score,
                         private val getFinishUsecase: GetFinishUsecase,
                         private val handler: Handler = Handler(),
                         starter: Boolean) : BaseViewModel(), TeamScoreListener {

    val finish = ObservableField<String>("")
    val nineDarter = ObservableBoolean(false)
    val started = ObservableBoolean(starter)
    val scored = ObservableInt(0)
    val score = ObservableField<Score>(startScore)
    val currentTeam = ObservableBoolean()
    private var currentPlayer : Player = team.players[0]

    private var isNineDarterStillPossible = true
    private var finishFuture: Future<*>? = null

    fun turnUpdate(next: Next) {
        updateNineDarter(next)
        updateLegStarter(next)
        updateCurrentTeam(next)
        updateCurrentPlayer(next.player)
        calculateFinish(score.get()!!, currentPlayer)
    }

    fun scored(input: Score, by: Player) {
        score.set(input.copy())
        removeScoredBadgeAfter(100)
        calculateFinish(input, by)
    }

    fun threw(turn: Turn, player: Player) {
        if (team.contains(player)) {
            scored.set(this.score.get()!!.score - turn.total())
            calculateFinish(this.score.get()!!, player, turn)
        }
    }

    private fun updateCurrentTeam(next: Next) {
        currentTeam.set(team.contains(next.player))
    }

    private fun updateCurrentPlayer(player: Player){
        if(team.contains(player)){
            currentPlayer = player
        }
    }

    private fun updateLegStarter(next: Next) {
        when {
            next.state == State.START -> started.set(team.contains(next.player))
            next.state == State.LEG -> started.set(team.contains(next.player))
            next.state == State.SET -> started.set(team.contains(next.player))
        }
    }

    private fun updateNineDarter(next: Next) {
        if (next.state == State.LEG || next.state == State.SET || next.state == State.START) {
            isNineDarterStillPossible = true
            nineDarter.set(false)
        }
    }

    override fun onNineDartEvent(event: NineDartEvent) {
        if (team.contains(event.by())) {
            nineDarter.set(event.isPossible() && isNineDarterStillPossible)
            isNineDarterStillPossible = event.isPossible()
        }
    }

    private fun calculateFinish(input: Score, player: Player, turn: Turn = Turn()) {
        if(team.contains(player)) {
            finishFuture?.cancel(true)
            finishFuture = getFinishUsecase.calculate(GetFinishRequest(input, turn, player.prefs.favoriteDouble)) {
                finish.set(it.finish)
            }
        }
    }

    private fun removeScoredBadgeAfter(duration: Long) {
        this.handler.postDelayed({
            scored.set(0)
        }, duration)
    }
}