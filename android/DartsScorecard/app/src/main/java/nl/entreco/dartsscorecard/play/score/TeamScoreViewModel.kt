package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.GetFinishUsecase
import java.util.concurrent.Future

/**
 * Created by Entreco on 22/11/2017.
 */
class TeamScoreViewModel(val team: Team, startScore: Score, private val getFinishUsecase: GetFinishUsecase, starter: Boolean) : BaseViewModel() {

    val finish = ObservableField<String>("")
    val started = ObservableBoolean(starter)
    val scored = ObservableInt(0)
    val score = ObservableField<Score>(startScore)
    val currentTeam = ObservableBoolean()

    private val handler = Handler()
    private var finishFuture: Future<*>? = null

    fun turnUpdate(next: Next) {
        updateLegStarter(next)
        updateCurrentTeam(next)
    }

    fun scored(input: Score, by: Player) {
        this.score.set(input.copy())
        removeScoredBadgeAfter(100)
        calculateFinish(input, by)
    }

    fun threw(turn: Turn, player: Player) {
        if (team.contains(player)) {
            scored.set(turn.total())
            calculateFinish(this.score.get(), player, turn)
        }
    }

    private fun updateCurrentTeam(next: Next) {
        currentTeam.set(team.contains(next.player))
    }

    private fun updateLegStarter(next: Next) {
        when {
            next.state == State.START -> started.set(team.contains(next.player))
            next.state == State.LEG -> started.set(team.contains(next.player))
            next.state == State.SET -> started.set(team.contains(next.player))
        }
    }

    private fun calculateFinish(input: Score, player: Player, turn: Turn = Turn()) {
        val cancelled = finishFuture?.cancel(true)
        finishFuture = getFinishUsecase.calculate(input, turn, player.prefs.favoriteDouble, { finish.set(it) })
    }

    private fun removeScoredBadgeAfter(duration: Long) {
        this.handler.postDelayed({ scored.set(0) }, duration)
    }
}