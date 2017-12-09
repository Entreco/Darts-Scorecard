package nl.entreco.domain.play.usecase

import android.support.annotation.VisibleForTesting
import android.support.annotation.WorkerThread
import nl.entreco.domain.Logger
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject

/**
 * Created by Entreco on 24/11/2017.
 */
open class GetFinishUsecase @Inject constructor(private val logger: Logger) {

    private val bg: ExecutorService = Executors.newSingleThreadExecutor()

    fun calculate(score: Score, turn: Turn, favDouble: Int, result: (String) -> Unit): Future<*> {
        return bg.submit {
            val finish = calculateInBack(score.score, turn.copy(), favDouble)
            result(finish)
        }
    }

    private val impossible = arrayOf(169, 168, 166, 165, 163, 162, 159)
    val notPossible = ""

    @WorkerThread
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    fun calculateInBack(score: Int, turn: Turn, favDouble: Int): String {

        // Calculate desired target
        val target = score - turn.total()

        // First, handle the obvious cases before we go into recursion ;)
        if (target > 170) return notPossible
        if (target <= 1) return notPossible
        if (impossible.contains(target)) return notPossible

        // Now, hardcode some values that do not need favourite Double, since
        // it can only be done in 1 way
        if (require(target, turn, 170, 3)) return "T20 T20 BULL"
        if (require(target, turn, 167, 3)) return "T20 T19 BULL"
        if (require(target, turn, 164, 3)) return "T20 T18 BULL"
        if (require(target, turn, 161, 3)) return "T20 T17 BULL"
        if (require(target, turn, 160, 3)) return "T20 T20 D20"
        if (require(target, turn, 158, 3)) return "T20 T20 D19"
        if (require(target, turn, 157, 3)) return "T20 T19 D20"
        if (require(target, turn, 156, 3)) return "T20 T20 D18"
        if (require(target, turn, 155, 3)) return "T20 T19 D19"
        if (require(target, turn, 154, 3)) return "T20 T18 D20"
        if (require(target, turn, 153, 3)) return "T20 T19 D18"
        if (require(target, turn, 152, 3)) return "T20 T20 D16"
        if (require(target, turn, 151, 3)) return "T20 T17 D20"
        if (require(target, turn, 150, 3)) return "T20 T18 D18"
        if (require(target, turn, 149, 3)) return "T20 T19 D16"
        if (require(target, turn, 148, 3)) return "T20 T16 D20"
        if (require(target, turn, 147, 3)) return "T19 T18 D18"
        if (require(target, turn, 146, 3)) return "T20 T18 D16"
        if (require(target, turn, 145, 3)) return "T20 T15 D20"
        if (require(target, turn, 144, 3)) return "T20 T20 D12"
        if (require(target, turn, 143, 3)) return "T20 T17 D16"
        if (require(target, turn, 142, 3)) return "T20 T14 D20"
        if (require(target, turn, 141, 3)) return "T20 T19 D12"
        if (require(target, turn, 140, 3)) return "T20 T16 D16"
        if (require(target, turn, 139, 3)) return "T19 T14 D20"
        if (require(target, turn, 138, 3)) return "T20 T18 D12"
        if (require(target, turn, 137, 3)) return "T19 T16 D16"
        if (require(target, turn, 136, 3)) return "T20 T20 D8"
        if (require(target, turn, 135, 3)) return "T20 T17 D12"
        if (require(target, turn, 134, 3)) return "T20 T14 D16"
        if (require(target, turn, 133, 3)) return "T20 T19 D8"
        if (require(target, turn, 132, 3)) return "T20 T16 D12"
        if (require(target, turn, 131, 3)) return "T20 T13 D16"
        if (require(target, turn, 130, 3)) return "T20 20 BULL"
        if (require(target, turn, 129, 3)) return "T19 T16 D12"
        if (require(target, turn, 128, 3)) return "T18 T14 D16"
        if (require(target, turn, 127, 3)) return "T20 T17 D8"
        if (require(target, turn, 126, 3)) return "T19 T19 D6"
        if (require(target, turn, 125, 3)) return "S.BULL T20 D20"
        if (require(target, turn, 124, 3)) return "T20 T16 D8"
        if (require(target, turn, 123, 3)) return "T19 T16 D9"
        if (require(target, turn, 122, 3)) return "T18 T20 D4"
        if (require(target, turn, 121, 3)) return "T17 T10 D20"
        if (require(target, turn, 120, 3)) return "T20 20 D20"
        if (require(target, turn, 119, 3)) return "T19 T10 D16"
        if (require(target, turn, 118, 3)) return "T20 18 D20"
        if (require(target, turn, 117, 3)) return "T20 17 D20"
        if (require(target, turn, 116, 3)) return "T20 16 D20"
        if (require(target, turn, 115, 3)) return "T20 15 D20"
        if (require(target, turn, 114, 3)) return "T20 14 D20"
        if (require(target, turn, 113, 3)) return "T20 13 D20"
        if (require(target, turn, 112, 3)) return "T20 12 D20"
        if (require(target, turn, 111, 3)) return "T20 19 D16"
        if (require(target, turn, 110, 3)) return "T20 18 D16"
        if (require(target, turn, 109, 3)) return "T19 20 D16"
        if (require(target, turn, 108, 3)) return "T20 16 D16"
        if (require(target, turn, 107, 3)) return "T19 18 D16"
        if (require(target, turn, 106, 3)) return "T20 14 D16"
        if (require(target, turn, 105, 3)) return "T19 16 D16"
        if (require(target, turn, 104, 3)) return "T18 18 D16"
        if (require(target, turn, 103, 3)) return "T20 3 D20"
        if (require(target, turn, 102, 3)) return "T20 10 D16"
        if (require(target, turn, 101, 3)) return "T20 1 D20"
        if (require(target, turn, 100, 2)) return "T20 D20"
        if (require(target, turn, 99, 3)) return "T19 10 D16"
        if (require(target, turn, 98, 2)) return "T19 D19"
        if (require(target, turn, 97, 2)) return "T19 D20"
        if (require(target, turn, 96, 2)) return "T20 D18"
        if (require(target, turn, 95, 2)) return "T19 D19"
        if (require(target, turn, 94, 2)) return "T18 D20"
        if (require(target, turn, 93, 2)) return "T19 D18"
        if (require(target, turn, 92, 2)) return "T20 D16"
        if (require(target, turn, 91, 2)) return "T17 D20"
        if (require(target, turn, 90, 2)) return "T20 D15"
        if (require(target, turn, 89, 2)) return "T19 D16"
        if (require(target, turn, 88, 2)) return "T16 D20"
        if (require(target, turn, 87, 2)) return "T17 D18"
        if (require(target, turn, 86, 2)) return "T18 D16"
        if (require(target, turn, 85, 2)) return "T15 D20"
        if (require(target, turn, 84, 2)) return "T20 D12"
        if (require(target, turn, 83, 2)) return "T17 D16"
        if (require(target, turn, 82, 2)) return "T14 D20"
        if (require(target, turn, 81, 2)) return "T19 D12"
        if (require(target, turn, 80, 2)) return "T20 D10"
        if (require(target, turn, 79, 2)) return "T13 D20"
        if (require(target, turn, 78, 2)) return "T18 D12"
        if (require(target, turn, 77, 2)) return "T19 D10"
        if (require(target, turn, 76, 2)) return "T20 D8"
        if (require(target, turn, 75, 2)) return "T17 D12"
        if (require(target, turn, 74, 2)) return "T18 D10"
        if (require(target, turn, 73, 2)) return "T19 D8"
        if (require(target, turn, 72, 2)) return "T12 D18"
        if (require(target, turn, 71, 2)) return "T17 D10"
        if (require(target, turn, 70, 2)) return "T10 D20"
        if (require(target, turn, 69, 2)) return "T13 D15"
        if (require(target, turn, 68, 2)) return "T20 D4"
        if (require(target, turn, 67, 2)) return "T17 D8"
        if (require(target, turn, 66, 2)) return "T10 D18"
        if (require(target, turn, 65, 2)) return "S.BULL D20"
        if (require(target, turn, 64, 2)) return "T16 D8"
        if (require(target, turn, 63, 2)) return "T13 D12"
        if (require(target, turn, 62, 2)) return "T10 D16"
        if (require(target, turn, 61, 2)) return "25 D18"
        if (require(target, turn, 60, 2)) return "20 D20"
        if (require(target, turn, 59, 2)) return "19 D20"
        if (require(target, turn, 58, 2)) return "18 D20"
        if (require(target, turn, 57, 2)) return "17 D20"
        if (require(target, turn, 56, 2)) return "T16 D4"
        if (require(target, turn, 55, 2)) return "15 D20"
        if (require(target, turn, 54, 2)) return "14 D20"
        if (require(target, turn, 53, 2)) return "13 D20"
        if (require(target, turn, 52, 2)) return "T12 D8"
        if (require(target, turn, 51, 2)) return "11 D20"
        if (require(target, turn, 50, 2)) return "10 D20"
        if (require(target, turn, 50, 1)) return "BULL" // Special case
        if (require(target, turn, 49, 2)) return "9 D20"
        if (require(target, turn, 48, 2)) return "16 D16"
        if (require(target, turn, 47, 2)) return "15 D16"
        if (require(target, turn, 46, 2)) return "6 D20"
        if (require(target, turn, 45, 2)) return "13 D16"
        if (require(target, turn, 44, 2)) return "12 D16"
        if (require(target, turn, 43, 2)) return "11 D16"
        if (require(target, turn, 42, 2)) return "10 D16"
        if (require(target, turn, 41, 2)) return "9 D16"
        if (require(target, turn, 40, 3)) return "8 D16"
        if (require(target, turn, 40, 1)) return "D20"
        if (require(target, turn, 39, 2)) return "7 D16"
        if (require(target, turn, 38, 3)) return "6 D16"
        if (require(target, turn, 38, 1)) return "D19"
        if (require(target, turn, 37, 2)) return "5 D16"
        if (require(target, turn, 36, 3)) return "4 D16"
        if (require(target, turn, 36, 1)) return "D18"
        if (require(target, turn, 35, 2)) return "3 D16"
        if (require(target, turn, 34, 3)) return "2 D16"
        if (require(target, turn, 34, 1)) return "D17"
        if (require(target, turn, 33, 2)) return "1 D16"
        if (require(target, turn, 32, 1)) return "D16"
        if (require(target, turn, 31, 2)) return "15 D8"
        if (require(target, turn, 30, 3)) return "14 D8"
        if (require(target, turn, 30, 1)) return "D15"
        if (require(target, turn, 29, 2)) return "13 D8"
        if (require(target, turn, 28, 2)) return "12 D8"
        if (require(target, turn, 28, 1)) return "D14"
        if (require(target, turn, 27, 2)) return "19 D4"
        if (require(target, turn, 26, 3)) return "18 D4"
        if (require(target, turn, 26, 1)) return "D13"
        if (require(target, turn, 25, 2)) return "17 D4"
        if (require(target, turn, 24, 3)) return "16 D4"
        if (require(target, turn, 24, 1)) return "D12"
        if (require(target, turn, 23, 2)) return "7 D8"
        if (require(target, turn, 22, 2)) return "6 D8"
        if (require(target, turn, 22, 1)) return "D11"
        if (require(target, turn, 21, 2)) return "5 D8"
        if (require(target, turn, 20, 1)) return "D10"
        if (require(target, turn, 19, 2)) return "11 D4"
        if (require(target, turn, 18, 3)) return "2 D8"
        if (require(target, turn, 18, 1)) return "D9"
        if (require(target, turn, 17, 2)) return "9 D4"
        if (require(target, turn, 16, 1)) return "D8"
        if (require(target, turn, 15, 2)) return "7 D4"
        if (require(target, turn, 14, 1)) return "D7"
        if (require(target, turn, 13, 2)) return "5 D4"
        if (require(target, turn, 12, 1)) return "D6"
        if (require(target, turn, 11, 2)) return "3 D4"
        if (require(target, turn, 10, 1)) return "D5"
        if (require(target, turn, 9, 2)) return "1 D4"
        if (require(target, turn, 8, 1)) return "D4"
        if (require(target, turn, 7, 2)) return "3 D2"
        if (require(target, turn, 6, 1)) return "D3"
        if (require(target, turn, 5, 2)) return "1 D2"
        if (require(target, turn, 4, 1)) return "D2"
        if (require(target, turn, 3, 2)) return "1 D1"
        if (require(target, turn, 2, 1)) return "D1"

        // Okay, now we have some options -> so let's go
        if (target == turn.total() && turn.last().isDouble()) return turn.asFinish()
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