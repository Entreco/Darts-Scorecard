package nl.entreco.dartsscorecard.play.bot

import org.mockito.kotlin.mock
import nl.entreco.domain.model.Dart
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.liblog.Logger
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import org.junit.Test
import java.util.Random

class CalculateBotScoreUsecaseTest {

    private val mockFg: nl.entreco.libcore.threading.Foreground = mock()
    private val mockBg: nl.entreco.libcore.threading.Background = mock()
    private val mockGetFinsihUsecsae: GetFinishUsecase = mock()
    private val mockLogger: Logger = mock()

    private val subject = CalculateBotScoreUsecase(mockLogger, mockGetFinsihUsecsae, mockBg, mockFg)

    @Test
    fun `should work with gaussian`() {
        val yes = (0..99).map {
            val target = Dart.random()
            val result = subject.generateSemiRandomDart(target.desc(), 3f)
            println("Target: $target\tthrown: $result\t${target == result}")
            target == result
        }.filter { it }.count()
        println("===== $yes / 100 = ${100 * yes/99.0f}%")
    }

    @Test
    fun `check T20`() {
        val yes = (0..99).map {
            val target = Dart.TRIPLE_20
            val result = subject.generateSemiRandomDart(target.desc(), 3f)
            println("Target: $target\tthrown: $result\t${target == result}")
            target == result
        }.filter { it }.count()
        println("===== $yes / 100 = ${100 * yes/99.0f}%")
    }

    @Test
    fun `check 20`() {
        val yes = (0..99).map {
            val target = Dart.SINGLE_20
            val result = subject.generateSemiRandomDart(target.desc(), 3f)
            println("Target: $target\tthrown: $result\t${target == result}")
            target == result
        }.filter { it }.count()
        println("===== $yes / 100 = ${100 * yes/99.0f}%")
    }

    @Test
    fun `test random`() {

        repeat((0..10).count()) {
            val avg = (0..10).map {
                Dart.random().points()
            }.average()

            println(avg)
        }
    }

    @Test
    fun `test gaussian`() {
        val avg = (0..100).map {
            val rand = Random().nextGaussian()
            val bucket = when {
                rand < -3   -> 4
                rand < -2   -> 3
                rand < -1   -> 2
                rand < -0.5 -> 1
                rand < 0.5  -> 0
                rand < 1    -> 1
                rand < 2    -> 2
                rand < 3    -> 3
                else        -> 4
            }
            println("gaus: $rand bucket: $bucket")
            bucket
        }.average()

        println("Overall Average: $avg")
    }
}