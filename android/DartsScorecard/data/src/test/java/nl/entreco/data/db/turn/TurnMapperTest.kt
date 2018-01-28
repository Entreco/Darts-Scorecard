package nl.entreco.data.db.turn

import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Turn
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 24/12/2017.
 */
class TurnMapperTest {

    private val subject = TurnMapper()
    private val gameId : Long = 1011
    private val playerId : Long = 42
    private lateinit var expectedTurn : Turn

    @Test
    fun `converting back and forth should return Turn()`() {
        whenConvertingFromAndTo(Turn())
    }

    @Test
    fun `converting back and forth should return Turn(ZERO)`() {
        whenConvertingFromAndTo(Turn(Dart.ZERO))
    }

    @Test
    fun `converting back and forth should return Turn(ZERO, NONE)`() {
        whenConvertingFromAndTo(Turn(Dart.ZERO, Dart.NONE))
    }


    @Test
    fun `converting back and forth should return Turn(20, T20, D20)`() {
        whenConvertingFromAndTo(Turn(Dart.SINGLE_20, Dart.TRIPLE_20, Dart.DOUBLE_20))
    }

    @Test
    fun `converting back and forth should return Turn(1, D15)`() {
        whenConvertingFromAndTo(Turn(Dart.SINGLE_1, Dart.DOUBLE_15))
    }

    private fun whenConvertingFromAndTo(turn: Turn) {
        val from = subject.from(gameId, playerId, turn)
        assertEquals(0, from.id)
        expectedTurn = subject.to(from)
        assertEquals("turn: $turn exp:$expectedTurn",turn, expectedTurn)
    }

}