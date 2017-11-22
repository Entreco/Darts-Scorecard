package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 22/11/2017.
 */
class TeamScoreViewModel(val team: Team, sc: Score) {

    val started = ObservableBoolean(false)
    val scored = ObservableInt(0)
    val score = ObservableField<Score>(sc)
    private val handler = Handler()

    fun scored(score: Score) {
        this.score.set(score)
        this.handler.postDelayed({ scored.set(0) }, 100)
    }

    fun threw(dart: Int) {
        scored.set(scored.get() + dart)
    }
}