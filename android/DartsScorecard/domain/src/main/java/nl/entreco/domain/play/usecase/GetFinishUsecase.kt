package nl.entreco.domain.play.usecase

import android.support.annotation.VisibleForTesting
import android.support.annotation.WorkerThread
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject

/**
 * Created by Entreco on 24/11/2017.
 */
open class GetFinishUsecase @Inject constructor() {

    private val bg: ExecutorService = Executors.newSingleThreadExecutor()

    fun calculate(score: Score, turn: Turn, favDouble: Int, result: (String) -> Unit): Future<*> {
        return bg.submit {
            val finish = calculateInBack(score.score, turn, favDouble)
            result(finish)
        }
    }

    private val impossible = arrayOf(169, 168, 166, 165, 163, 162, 159)
    private val notPossible = ""

    @WorkerThread
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    fun calculateInBack(score: Int, turn: Turn, favDouble: Int): String {

        // First, handle the obvious cases before we go into recursion ;)
        if (score > 170) return notPossible
        if (score <= 1) return notPossible
        if (impossible.contains(score)) return notPossible

        // Now, hardcode some values that do not need favourite Double, since
        // it can only be done in 1 way
        if (score == 170 && turn.dartsLeft() >= 3) return "T20 T20 BULL"
        if (score == 167 && turn.dartsLeft() >= 3) return "T20 T19 BULL"
        if (score == 164 && turn.dartsLeft() >= 3) return "T20 T18 BULL"
        if (score == 2 && turn.dartsLeft() >= 1) return "D1"
        if (score == 3 && turn.dartsLeft() >= 1) return "1 D1"

        // Okay, now we have some options -> so let's go
        if (score == turn.total() && turn.last().isDouble()) return turn.asFinish()
        if (turn.dartsLeft() == 0) return notPossible


        // TODO: Try Doubles + BULL
        // TODO: If fails, add 1 dart for all singles, triples

        // TODO: Recurse;)

        // All Doubles, + 1 BULL, All Singles, S.BULL, All Tripples
        return notPossible
    }
}