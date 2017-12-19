package nl.entreco.domain.model

import nl.entreco.domain.TestProvider
import org.junit.Test

class GameWithLegTest : BaseGameTest(Game(0, TestProvider().arbiter(201))) {

    @Test
    fun `it should print correct start score`() {
        assertScore(201, 201)
    }

    @Test
    fun `it should restart when player 1 finished`() {
        givenGameStarted()
        whenDartsThrown(oneEighty(), sixty(), Turn(Dart.SINGLE_1, Dart.DOUBLE_10))

        assertScore(201, 201, 1)
    }

    @Test
    fun `it should restart when player 2 finishes`() {
        givenGameStarted()
        whenDartsThrown(sixty(), oneEighty(), sixty(), Turn(Dart.SINGLE_1, Dart.DOUBLE_10))

        assertScore(201, 201, 1)
    }
}