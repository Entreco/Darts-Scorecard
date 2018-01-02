package nl.entreco.domain.setup

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.executors.TestBackground
import nl.entreco.domain.executors.TestForeground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository
import nl.entreco.domain.setup.usecase.FetchExistingPlayersUsecase
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FetchExistingPlayersUsecaseTest {

    @Mock private lateinit var mockRepo: PlayerRepository
    @Mock private lateinit var mockDone: (List<Player>) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    private val bg = TestBackground()
    private val fg = TestForeground()
    private val playersCaptor = argumentCaptor<List<Player>>()
    private lateinit var subject: FetchExistingPlayersUsecase

    private lateinit var expectedPlayers: List<Player>
    private lateinit var expectedError: Throwable

    @Test
    fun `it should report emptyList when fetching players succeeds`() {
        givenSubject()
        whenFetchingPlayersSucceeds(emptyList())
        thenDoneIsCalledWithPlayers()
    }

    @Test
    fun `it should report player when fetching players succeeds`() {
        givenSubject()
        whenFetchingPlayersSucceeds(listOf(Player("piet")))
        thenDoneIsCalledWithPlayers("piet")
    }

    @Test
    fun `it should report players when fetching players succeeds`() {
        givenSubject()
        whenFetchingPlayersSucceeds(listOf(Player("piet"), Player("henk")))
        thenDoneIsCalledWithPlayers("piet", "henk")
    }

    @Test
    fun `it should report error when fetching players fails`() {
        givenSubject()
        whenFetchingPlayersFails(IllegalStateException("unable to retrieve players"))
        thenErrorIsReported()
    }

    private fun givenSubject() {
        subject = FetchExistingPlayersUsecase(mockRepo, bg, fg)
    }

    private fun whenFetchingPlayersSucceeds(players: List<Player>) {
        expectedPlayers = players
        whenever(mockRepo.fetchAll()).thenReturn(players)

        subject.exec(mockDone, mockFail)
        verify(mockRepo).fetchAll()
    }

    private fun whenFetchingPlayersFails(err: Throwable) {
        expectedError = err
        whenever(mockRepo.fetchAll()).thenThrow(err)

        subject.exec(mockDone, mockFail)
        verify(mockRepo).fetchAll()
    }

    private fun thenDoneIsCalledWithPlayers(vararg names: String) {
        verify(mockDone).invoke(playersCaptor.capture())
        val players: List<String> = playersCaptor.lastValue.map { it.name }
        names.forEach {
            assertTrue(players.contains(it))
        }
        players.forEach { assertTrue(names.contains(it)) }
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(expectedError)
    }

}