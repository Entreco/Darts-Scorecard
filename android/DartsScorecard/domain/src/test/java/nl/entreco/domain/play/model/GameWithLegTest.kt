package nl.entreco.domain.play.model

import nl.entreco.domain.play.TestProvider
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Assert.assertEquals
import org.junit.Test

class GameWithLegTest : BaseGameTest(Game("uid", Arbiter(Score(201, settings = ScoreSettings(201)), TestProvider().turnHandler()))) {

    @Test
    fun `it should print correct start score`() {
        assertScore(201, 201)
    }

    @Test
    fun `it should restart when player 1 finished`() {
        givenGameStarted()
        whenDartsThrown(oneEighty(), sixty(), Turn(Dart.SINGLE_1, Dart.DOUBLE_10))

        assertScore(201, 201, 1)
        assertEquals("player 2 to throw", subject.state)
    }

    @Test
    fun `it should restart when player 2 finishes`() {
        givenGameStarted()
        whenDartsThrown(sixty(), oneEighty(), sixty(), Turn(Dart.SINGLE_1, Dart.DOUBLE_10))

        assertScore(201, 201, 1)
        assertEquals("player 2 to throw", subject.state)
    }
}