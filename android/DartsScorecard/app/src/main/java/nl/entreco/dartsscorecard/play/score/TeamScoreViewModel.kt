package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.util.Log
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 22/11/2017.
 */
class TeamScoreViewModel(val team: Team, startScore: Score) {

    val finish = ObservableField<String>("")
    val started = ObservableBoolean(false)
    val scored = ObservableInt(0)
    val score = ObservableField<Score>(startScore)
    private val handler = Handler()

    fun scored(input: Score) {
        Log.d("NoNice", "input:$input, score:${score.get()}")
        val pointsScored = this.score.get().score - input.score
        Log.d("NoNice", "points:$pointsScored")
        this.finish.set(random(input))
        this.score.set(input.copy())
        if(pointsScored == 180){
            scored.set(180)
            clearScored(1000)
        } else {
            clearScored(100)
        }
    }

    private fun clearScored(duration: Long) {
        this.handler.postDelayed({ scored.set(0) }, duration)
    }

    private fun random(score: Score) : String {
        return when {
            score.score > 170 -> ""
            score.score > 100 -> "T20 BULL"
            score.score > 40 -> "D20"
            else -> ""
        }
    }

    fun threw(dart: Int) {
        scored.set(scored.get() + dart)
    }
}