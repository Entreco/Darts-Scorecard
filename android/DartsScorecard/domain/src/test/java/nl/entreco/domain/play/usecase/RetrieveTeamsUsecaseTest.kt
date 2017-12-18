package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.play.TestBackground
import nl.entreco.domain.play.TestForeground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.model.players.TeamIdsString
import nl.entreco.domain.repository.PlayerRepository
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
    private lateinit var teamIds: TeamIdsString

    @Mock private lateinit var ok : (Array<Team>) -> Unit
    @Mock private lateinit var fail : (Throwable) -> Unit
    @Mock private lateinit var mockPlayerRepository: PlayerRepository
    @Mock private lateinit var mockPlayer: Player

    private lateinit var subject: RetrieveTeamsUsecase

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
    fun `it should fail if some players cannot be retrieved`() {
        givenPlayersWithIds("")
        whenExecuting()
        thenPlayersNotAreRetrieved()
        thenFailedIsInvoked()
    }

    private fun givenPlayersWithIds(teams: String) {
        teamIds = TeamIdsString(teams)
        whenever(mockPlayerRepository.fetchById(any())).thenReturn(mockPlayer)
    }

    private fun whenExecuting() {
        subject.start(teamIds, ok, fail)
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

}