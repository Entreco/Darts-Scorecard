package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.util.Log
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import java.util.concurrent.Future

/**
 * Created by Entreco on 22/11/2017.
 */
class TeamScoreViewModel(val team: Team, startScore: Score, private val finishCalculator: FinishCalculator) {

    val finish = ObservableField<String>("")
    val started = ObservableBoolean(false)
    val scored = ObservableInt(0)
    val score = ObservableField<Score>(startScore)
    private val handler = Handler()
    private var finishFuture: Future<*>? = null

    fun scored(input: Score, player: Player) {

        this.score.set(input.copy())

        removeScoredBadgeAfter(100)
        calculateFinish(input, player)
    }

    fun threw(dart: Int) {
        scored.set(scored.get() + dart)
    }

    private fun calculateFinish(input: Score, player: Player) {
        val cancelled = finishFuture?.cancel(true)
        Log.d("NoNice", "calculateFinish cancelled:$cancelled")
        finishFuture = finishCalculator.calculate(input, player.prefs.favoriteDouble, { finish.set(it) })
    }

    private fun removeScoredBadgeAfter(duration: Long) {
        this.handler.postDelayed({ scored.set(0) }, duration)
    }
}