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
        if (require(score, turn, 170, 3)) return "T20 T20 BULL"
        if (require(score, turn, 167, 3)) return "T20 T19 BULL"
        if (require(score, turn, 164, 3)) return "T20 T18 BULL"
        if (require(score, turn, 161, 3)) return "T20 T17 BULL"
        if (require(score, turn, 160, 3)) return "T20 T20 D20"
        if (require(score, turn, 158, 3)) return "T20 T20 D19"
        if (require(score, turn, 157, 3)) return "T20 T19 D20"
        if (require(score, turn, 156, 3)) return "T20 T20 D18"
        if (require(score, turn, 155, 3)) return "T20 T19 D19"
        if (require(score, turn, 154, 3)) return "T20 T18 D20"
        if (require(score, turn, 153, 3)) return "T20 T19 D18"
        if (require(score, turn, 152, 3)) return "T20 T20 D16"
        if (require(score, turn, 151, 3)) return "T20 T17 D20"
        if (require(score, turn, 150, 3)) return "T20 T18 D18"
        if (require(score, turn, 149, 3)) return "T20 T19 D16"
        if (require(score, turn, 148, 3)) return "T20 T16 D20"
        if (require(score, turn, 147, 3)) return "T19 T18 D18"
        if (require(score, turn, 146, 3)) return "T20 T18 D16"
        if (require(score, turn, 145, 3)) return "T20 T15 D20"


        if (require(score, turn, 80, 2)) return "T20 D10"
        if (require(score, turn, 79, 2)) return "T13 D20"
        if (require(score, turn, 78, 2)) return "T18 D12"
        if (require(score, turn, 77, 2)) return "T19 D10"
        if (require(score, turn, 76, 2)) return "T20 D8"
        if (require(score, turn, 75, 2)) return "T17 D12"
        if (require(score, turn, 74, 2)) return "T18 D10"
        if (require(score, turn, 73, 2)) return "T19 D8"
        if (require(score, turn, 72, 2)) return "T12 D18"
        if (require(score, turn, 71, 2)) return "T17 D10"
        if (require(score, turn, 70, 2)) return "T10 D20"
        if (require(score, turn, 69, 2)) return "T13 D15"
        if (require(score, turn, 68, 2)) return "T20 D4"
        if (require(score, turn, 67, 2)) return "T17 D8"
        if (require(score, turn, 66, 2)) return "T10 D18"
        if (require(score, turn, 65, 2)) return "S.BULL D20"
        if (require(score, turn, 64, 2)) return "T16 D8"
        if (require(score, turn, 63, 2)) return "T13 D12"
        if (require(score, turn, 62, 2)) return "T10 D16"
        if (require(score, turn, 61, 2)) return "25 D18"
        if (require(score, turn, 60, 2)) return "20 D20"


        // Okay, now we have some options -> so let's go
        if (score == turn.total() && turn.last().isDouble()) return turn.asFinish()
        if (turn.dartsLeft() == 0) return notPossible


        // TODO: Try Doubles + BULL
        // TODO: If fails, add 1 dart for all singles, triples

        // TODO: Recurse;)

        // All Doubles, + 1 BULL, All Singles, S.BULL, All Tripples
        return notPossible
    }

    private fun require(score: Int, turn: Turn, target: Int, darts: Int) =
            score == target && turn.dartsLeft() >= darts
}