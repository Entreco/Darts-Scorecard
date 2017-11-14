package nl.entreco.domain.play

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 14/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class CreateGameUsecaseTest {

    private lateinit var subject : CreateGameUsecase
    private val arbiter = Arbiter(Score(), 2)

    @Test
    fun `it should create a game and start it`() {
        givenNewGame()
        assertEquals("player 1 to throw first", subject.start().state)
    }

    private fun givenNewGame() {
        subject = CreateGameUsecase(arbiter)
    }

}