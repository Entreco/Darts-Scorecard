package nl.entreco.domain.play.start

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.model.players.DeletedPlayer
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class RetrieveTeamsUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var teamIds: String

    @Mock private lateinit var ok: (RetrieveTeamsResponse) -> Unit
    @Mock private lateinit var fail: (Throwable) -> Unit
    @Mock private lateinit var mockPlayerRepository: PlayerRepository
    @Mock private lateinit var mockPlayer: Player

    private lateinit var subject: RetrieveTeamsUsecase
    private val retrieveCaptor = argumentCaptor<RetrieveTeamsResponse>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = RetrieveTeamsUsecase(mockPlayerRepository, bg, fg)
    }

    @Test
    fun `it should fetch all players from db `() {
        givenPlayersWithIds("1,2|3|4,8|6")
        whenExecuting()
        thenPlayersAreRetrieved()
        thenOkIsInvoked()
    }

    @Test
    fun `it should add 'DeletedPlayer' if player id cannot be found`() {
        givenNonExistingPlayer()
        whenExecuting()
        thenDeletedPlayerIsReturned()
    }

    @Test
    fun `it should fail if some players cannot be retrieved`() {
        givenPlayersWithIds("")
        whenExecuting()
        thenPlayersNotAreRetrieved()
        thenFailedIsInvoked()
    }

    private fun givenPlayersWithIds(teams: String) {
        teamIds = teams
        whenever(mockPlayerRepository.fetchById(any())).thenReturn(mockPlayer)
    }

    private fun givenNonExistingPlayer() {
        teamIds = "1,2"
        whenever(mockPlayerRepository.fetchById(any())).thenReturn(null)
    }

    private fun whenExecuting() {
        subject.exec(RetrieveTeamsRequest(teamIds), ok, fail)
    }

    private fun thenPlayersAreRetrieved(vararg ids: Long) {
        ids.forEach {
            verify(mockPlayerRepository).fetchById(it)
        }
    }

    private fun thenPlayersNotAreRetrieved() {
        verify(mockPlayerRepository, atMost(1)).fetchById(any())
    }

    private fun thenOkIsInvoked() {
        verify(ok).invoke(isA())
    }

    private fun thenFailedIsInvoked() {
        verify(fail).invoke(isA())
    }

    private fun thenDeletedPlayerIsReturned() {
        verify(ok).invoke(retrieveCaptor.capture())
        assertEquals(1, retrieveCaptor.lastValue.teams.filter { it.contains(0) }.size)
        assertTrue(retrieveCaptor.lastValue.teams[0].players[0] is DeletedPlayer)
    }

}