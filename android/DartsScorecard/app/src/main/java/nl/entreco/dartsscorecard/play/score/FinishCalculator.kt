package nl.entreco.dartsscorecard.play.score

import android.os.SystemClock
import android.support.annotation.WorkerThread
import nl.entreco.domain.play.model.Score
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject

/**
 * Created by Entreco on 24/11/2017.
 */
class FinishCalculator @Inject constructor() {

    private val bg: ExecutorService = Executors.newSingleThreadExecutor()

    fun calculate(score: Score, favDouble: Int, result: (String) -> Unit): Future<*> {
        return bg.submit {
            val finish = calculateInBack(score, favDouble)
            result(finish)
        }
    }

    @WorkerThread
    private fun calculateInBack(score: Score, favDouble: Int): String {

        return when {
            score.score > 170 -> ""
            score.score == 170 -> "T20 T20 BULL"
            score.score > 100 -> "T20 BULL"
            score.score > 40 -> "D20"
            else -> ""
        }
    }
}