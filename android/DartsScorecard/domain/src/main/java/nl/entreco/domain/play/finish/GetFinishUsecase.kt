package nl.entreco.domain.play.finish

import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import nl.entreco.shared.threading.Background
import nl.entreco.domain.model.Dart
import java.util.concurrent.Future
import javax.inject.Inject

/**
 * Created by Entreco on 24/11/2017.
 */
class GetFinishUsecase @Inject constructor(private val bg: Background) {

    fun calculate(request: GetFinishRequest, result: (GetFinishResponse) -> Unit): Future<*>? {
        return bg.post(Runnable {
            val finish = calculateInBack(request.score(), request.dartsLeft(), request.total(), request.double())
            result(GetFinishResponse(finish))
        })
    }

    private val impossible = arrayOf(169, 168, 166, 165, 163, 162, 159)
    val notPossible = ""

    @WorkerThread
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    fun calculateInBack(score: Int, dartsLeft: Int, total: Int, favDouble: Int): String {

        // Calculate desired target
        val target = score - total

        // First, handle the obvious cases before we go into recursion ;)
        if (target > 170) return notPossible
        if (target <= 1) return notPossible
        if (impossible.contains(target)) return notPossible

        // These can only be done in 1 way -> just return that one
        if (require(target, dartsLeft, 170, 3)) return "T20 T20 BULL"
        if (require(target, dartsLeft, 167, 3)) return "T20 T19 BULL"
        if (require(target, dartsLeft, 164, 3)) return "T20 T18 BULL"
        if (require(target, dartsLeft, 161, 3)) return "T20 T17 BULL"
        if (require(target, dartsLeft, 160, 3)) return "T20 T20 D20"
        if (require(target, dartsLeft, 158, 3)) return "T20 T20 D19"
        if (require(target, dartsLeft, 157, 3)) return "T20 T19 D20"
        if (require(target, dartsLeft, 156, 3)) return "T20 T20 D18"
        if (require(target, dartsLeft, 155, 3)) return "T20 T19 D19"
        if (require(target, dartsLeft, 154, 3)) return "T20 T18 D20"
        if (require(target, dartsLeft, 153, 3)) return "T20 T19 D18"
        if (require(target, dartsLeft, 152, 3)) return "T20 T20 D16"
        if (require(target, dartsLeft, 151, 3)) return "T20 T17 D20"

        return when (favDouble) {
            0 -> calculateNormalOuts(target, dartsLeft)
            else -> calculatePersonalOuts(target, dartsLeft, favDouble)
        }
    }

    private fun calculatePersonalOuts(target: Int, dartsLeft: Int, fav: Int): String {
        val favScore = 2 * fav
        val favDouble = when(fav){
            25 -> "BULL"
            else -> "D$fav"
        }

        if (dartsLeft >= 1 && target == favScore) return favDouble

        if (dartsLeft >= 2)
            findDart(target - favScore)?.let { dart ->
                return "${dart.desc()} $favDouble"
            }

        if (dartsLeft >= 3) {
            Dart.values().forEach { dart1 ->
                findDart(target - dart1.points() - favScore)?.let { dart2 ->
                    return "${dart2.desc()} ${dart1.desc()} $favDouble"
                }
            }
        }

        return calculateNormalOuts(target, dartsLeft)
    }

    private fun findDart(rest: Int): Dart? {
        return Dart.values().firstOrNull {
            it.points() == rest
        }
    }

    private fun calculateNormalOuts(target: Int, dartsLeft: Int): String {
        // Now, hardcode some values that do not need favourite Double, since
        // it can only be done in 1 way
        if (require(target, dartsLeft, 150, 3)) return "T20 T18 D18"
        if (require(target, dartsLeft, 149, 3)) return "T20 T19 D16"
        if (require(target, dartsLeft, 148, 3)) return "T20 T16 D20"
        if (require(target, dartsLeft, 147, 3)) return "T19 T18 D18"
        if (require(target, dartsLeft, 146, 3)) return "T20 T18 D16"
        if (require(target, dartsLeft, 145, 3)) return "T20 T15 D20"
        if (require(target, dartsLeft, 144, 3)) return "T20 T20 D12"
        if (require(target, dartsLeft, 143, 3)) return "T20 T17 D16"
        if (require(target, dartsLeft, 142, 3)) return "T20 T14 D20"
        if (require(target, dartsLeft, 141, 3)) return "T20 T19 D12"
        if (require(target, dartsLeft, 140, 3)) return "T20 T16 D16"
        if (require(target, dartsLeft, 139, 3)) return "T19 T14 D20"
        if (require(target, dartsLeft, 138, 3)) return "T20 T18 D12"
        if (require(target, dartsLeft, 137, 3)) return "T19 T16 D16"
        if (require(target, dartsLeft, 136, 3)) return "T20 T20 D8"
        if (require(target, dartsLeft, 135, 3)) return "T20 T17 D12"
        if (require(target, dartsLeft, 134, 3)) return "T20 T14 D16"
        if (require(target, dartsLeft, 133, 3)) return "T20 T19 D8"
        if (require(target, dartsLeft, 132, 3)) return "T20 T16 D12"
        if (require(target, dartsLeft, 131, 3)) return "T20 T13 D16"
        if (require(target, dartsLeft, 130, 3)) return "T20 20 BULL"
        if (require(target, dartsLeft, 129, 3)) return "T19 T16 D12"
        if (require(target, dartsLeft, 128, 3)) return "T18 T14 D16"
        if (require(target, dartsLeft, 127, 3)) return "T20 T17 D8"
        if (require(target, dartsLeft, 126, 3)) return "T19 T19 D6"
        if (require(target, dartsLeft, 125, 3)) return "S.BULL T20 D20"
        if (require(target, dartsLeft, 124, 3)) return "T20 T16 D8"
        if (require(target, dartsLeft, 123, 3)) return "T19 T16 D9"
        if (require(target, dartsLeft, 122, 3)) return "T18 T20 D4"
        if (require(target, dartsLeft, 121, 3)) return "T17 T10 D20"
        if (require(target, dartsLeft, 120, 3)) return "T20 20 D20"
        if (require(target, dartsLeft, 119, 3)) return "T19 T10 D16"
        if (require(target, dartsLeft, 118, 3)) return "T20 18 D20"
        if (require(target, dartsLeft, 117, 3)) return "T20 17 D20"
        if (require(target, dartsLeft, 116, 3)) return "T20 16 D20"
        if (require(target, dartsLeft, 115, 3)) return "T20 15 D20"
        if (require(target, dartsLeft, 114, 3)) return "T20 14 D20"
        if (require(target, dartsLeft, 113, 3)) return "T20 13 D20"
        if (require(target, dartsLeft, 112, 3)) return "T20 12 D20"
        if (require(target, dartsLeft, 111, 3)) return "T20 19 D16"
        if (require(target, dartsLeft, 110, 3)) return "T20 18 D16"
        if (require(target, dartsLeft, 110, 2)) return "T20 BULL"
        if (require(target, dartsLeft, 109, 3)) return "T19 20 D16"
        if (require(target, dartsLeft, 108, 3)) return "T20 16 D16"
        if (require(target, dartsLeft, 107, 3)) return "T19 18 D16"
        if (require(target, dartsLeft, 107, 2)) return "T19 BULL"
        if (require(target, dartsLeft, 106, 3)) return "T20 14 D16"
        if (require(target, dartsLeft, 105, 3)) return "T19 16 D16"
        if (require(target, dartsLeft, 104, 3)) return "T18 18 D16"
        if (require(target, dartsLeft, 104, 2)) return "T18 BULL"
        if (require(target, dartsLeft, 103, 3)) return "T20 3 D20"
        if (require(target, dartsLeft, 102, 3)) return "T20 10 D16"
        if (require(target, dartsLeft, 101, 3)) return "T20 1 D20"
        if (require(target, dartsLeft, 101, 2)) return "T17 BULL"
        if (require(target, dartsLeft, 100, 2)) return "T20 D20"
        if (require(target, dartsLeft, 99, 3)) return "T19 10 D16"
        if (require(target, dartsLeft, 98, 2)) return "T20 D19"
        if (require(target, dartsLeft, 97, 2)) return "T19 D20"
        if (require(target, dartsLeft, 96, 2)) return "T20 D18"
        if (require(target, dartsLeft, 95, 2)) return "T19 D19"
        if (require(target, dartsLeft, 94, 2)) return "T18 D20"
        if (require(target, dartsLeft, 93, 2)) return "T19 D18"
        if (require(target, dartsLeft, 92, 2)) return "T20 D16"
        if (require(target, dartsLeft, 91, 2)) return "T17 D20"
        if (require(target, dartsLeft, 90, 2)) return "T20 D15"
        if (require(target, dartsLeft, 89, 2)) return "T19 D16"
        if (require(target, dartsLeft, 88, 2)) return "T16 D20"
        if (require(target, dartsLeft, 87, 2)) return "T17 D18"
        if (require(target, dartsLeft, 86, 2)) return "T18 D16"
        if (require(target, dartsLeft, 85, 2)) return "T15 D20"
        if (require(target, dartsLeft, 84, 2)) return "T20 D12"
        if (require(target, dartsLeft, 83, 2)) return "T17 D16"
        if (require(target, dartsLeft, 82, 2)) return "T14 D20"
        if (require(target, dartsLeft, 81, 2)) return "T19 D12"
        if (require(target, dartsLeft, 80, 2)) return "T20 D10"
        if (require(target, dartsLeft, 79, 2)) return "T13 D20"
        if (require(target, dartsLeft, 78, 2)) return "T18 D12"
        if (require(target, dartsLeft, 77, 2)) return "T19 D10"
        if (require(target, dartsLeft, 76, 2)) return "T20 D8"
        if (require(target, dartsLeft, 75, 2)) return "T17 D12"
        if (require(target, dartsLeft, 74, 2)) return "T18 D10"
        if (require(target, dartsLeft, 73, 2)) return "T19 D8"
        if (require(target, dartsLeft, 72, 2)) return "T12 D18"
        if (require(target, dartsLeft, 71, 2)) return "T17 D10"
        if (require(target, dartsLeft, 70, 2)) return "20 BULL"
        if (require(target, dartsLeft, 70, 3)) return "T10 D20"
        if (require(target, dartsLeft, 69, 2)) return "T13 D15"
        if (require(target, dartsLeft, 68, 2)) return "T20 D4"
        if (require(target, dartsLeft, 67, 2)) return "T17 D8"
        if (require(target, dartsLeft, 66, 2)) return "T10 D18"
        if (require(target, dartsLeft, 65, 2)) return "S.BULL D20"
        if (require(target, dartsLeft, 64, 2)) return "T16 D8"
        if (require(target, dartsLeft, 63, 2)) return "T13 D12"
        if (require(target, dartsLeft, 62, 2)) return "T10 D16"
        if (require(target, dartsLeft, 61, 2)) return "S.BULL D18"
        if (require(target, dartsLeft, 60, 2)) return "20 D20"
        if (require(target, dartsLeft, 59, 2)) return "19 D20"
        if (require(target, dartsLeft, 58, 2)) return "18 D20"
        if (require(target, dartsLeft, 57, 2)) return "17 D20"
        if (require(target, dartsLeft, 56, 2)) return "T16 D4"
        if (require(target, dartsLeft, 55, 2)) return "15 D20"
        if (require(target, dartsLeft, 54, 2)) return "14 D20"
        if (require(target, dartsLeft, 53, 2)) return "13 D20"
        if (require(target, dartsLeft, 52, 2)) return "T12 D8"
        if (require(target, dartsLeft, 51, 2)) return "11 D20"
        if (require(target, dartsLeft, 50, 2)) return "10 D20"
        if (require(target, dartsLeft, 50, 1)) return "BULL" // Special case
        if (require(target, dartsLeft, 49, 2)) return "9 D20"
        if (require(target, dartsLeft, 48, 2)) return "16 D16"
        if (require(target, dartsLeft, 47, 2)) return "15 D16"
        if (require(target, dartsLeft, 46, 2)) return "6 D20"
        if (require(target, dartsLeft, 45, 2)) return "13 D16"
        if (require(target, dartsLeft, 44, 2)) return "12 D16"
        if (require(target, dartsLeft, 43, 2)) return "11 D16"
        if (require(target, dartsLeft, 42, 2)) return "10 D16"
        if (require(target, dartsLeft, 41, 2)) return "9 D16"
        if (require(target, dartsLeft, 40, 3)) return "8 D16"
        if (require(target, dartsLeft, 40, 1)) return "D20"
        if (require(target, dartsLeft, 39, 2)) return "7 D16"
        if (require(target, dartsLeft, 38, 3)) return "6 D16"
        if (require(target, dartsLeft, 38, 1)) return "D19"
        if (require(target, dartsLeft, 37, 2)) return "5 D16"
        if (require(target, dartsLeft, 36, 3)) return "4 D16"
        if (require(target, dartsLeft, 36, 1)) return "D18"
        if (require(target, dartsLeft, 35, 2)) return "3 D16"
        if (require(target, dartsLeft, 34, 3)) return "2 D16"
        if (require(target, dartsLeft, 34, 1)) return "D17"
        if (require(target, dartsLeft, 33, 2)) return "1 D16"
        if (require(target, dartsLeft, 32, 1)) return "D16"
        if (require(target, dartsLeft, 31, 2)) return "15 D8"
        if (require(target, dartsLeft, 30, 3)) return "14 D8"
        if (require(target, dartsLeft, 30, 1)) return "D15"
        if (require(target, dartsLeft, 29, 2)) return "13 D8"
        if (require(target, dartsLeft, 28, 2)) return "12 D8"
        if (require(target, dartsLeft, 28, 1)) return "D14"
        if (require(target, dartsLeft, 27, 2)) return "19 D4"
        if (require(target, dartsLeft, 26, 3)) return "18 D4"
        if (require(target, dartsLeft, 26, 1)) return "D13"
        if (require(target, dartsLeft, 25, 2)) return "17 D4"
        if (require(target, dartsLeft, 24, 3)) return "16 D4"
        if (require(target, dartsLeft, 24, 1)) return "D12"
        if (require(target, dartsLeft, 23, 2)) return "7 D8"
        if (require(target, dartsLeft, 22, 2)) return "6 D8"
        if (require(target, dartsLeft, 22, 1)) return "D11"
        if (require(target, dartsLeft, 21, 2)) return "5 D8"
        if (require(target, dartsLeft, 20, 1)) return "D10"
        if (require(target, dartsLeft, 19, 2)) return "11 D4"
        if (require(target, dartsLeft, 18, 3)) return "2 D8"
        if (require(target, dartsLeft, 18, 1)) return "D9"
        if (require(target, dartsLeft, 17, 2)) return "9 D4"
        if (require(target, dartsLeft, 16, 1)) return "D8"
        if (require(target, dartsLeft, 15, 2)) return "7 D4"
        if (require(target, dartsLeft, 14, 1)) return "D7"
        if (require(target, dartsLeft, 13, 2)) return "5 D4"
        if (require(target, dartsLeft, 12, 1)) return "D6"
        if (require(target, dartsLeft, 11, 2)) return "3 D4"
        if (require(target, dartsLeft, 10, 1)) return "D5"
        if (require(target, dartsLeft, 9, 2)) return "1 D4"
        if (require(target, dartsLeft, 8, 1)) return "D4"
        if (require(target, dartsLeft, 7, 2)) return "3 D2"
        if (require(target, dartsLeft, 6, 1)) return "D3"
        if (require(target, dartsLeft, 5, 2)) return "1 D2"
        if (require(target, dartsLeft, 4, 1)) return "D2"
        if (require(target, dartsLeft, 3, 2)) return "1 D1"
        if (require(target, dartsLeft, 2, 1)) return "D1"
        return notPossible
    }

    private fun require(score: Int, dartsLeft: Int, target: Int, darts: Int) =
            score == target && dartsLeft >= darts
}