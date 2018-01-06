package nl.entreco.domain.launch.usecase

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class ExtractTeamsUsecaseTest {

    @Mock private lateinit var mockPlayerRepository: PlayerRepository
    @Mock private lateinit var mockOk: (TeamIdsString) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    private lateinit var subject: ExtractTeamsUsecase

    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var givenTeamNames: TeamNamesString

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = ExtractTeamsUsecase(mockPlayerRepository, bg, fg)
    }

    @Test
    fun `it should extract teams, and create for non-existing players`() {
        givenTeams("remco,piet|henk")
        whenStartingUsecase()
        thenCallbackIsNotified("0,1|2")
    }

    @Test
    fun `it should extract teams for existing players`() {
        givenTeams("remco,piet|henk")
        whenStartingUsecaseWithExisting()
        thenCallbackIsNotified("0,1|2")
    }

    private fun givenTeams(teamString: String) {
        givenTeamNames = TeamNamesString(teamString)
    }

    private fun whenStartingUsecase() {
        for ((count, player) in givenTeamNames.toPlayerNames().withIndex()) {
            whenever(mockPlayerRepository.fetchByName(player)).thenReturn(null)
            whenever(mockPlayerRepository.create(player, 0)).thenReturn(count.toLong())
        }
        subject.exec(givenTeamNames, mockOk, mockFail)
    }

    private fun whenStartingUsecaseWithExisting() {
        for ((count, player) in givenTeamNames.toPlayerNames().withIndex()) {
            whenever(mockPlayerRepository.fetchByName(player)).thenReturn(Player(player, count.toLong()))
        }
        subject.exec(givenTeamNames, mockOk, mockFail)
    }

    private fun thenCallbackIsNotified(expected: String) {
        verify(mockOk).invoke(TeamIdsString(expected))
    }

}