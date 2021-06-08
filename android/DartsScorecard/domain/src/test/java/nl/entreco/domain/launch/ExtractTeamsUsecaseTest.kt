package nl.entreco.domain.launch

import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.libcore.threading.TestBackground
import nl.entreco.libcore.threading.TestForeground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class ExtractTeamsUsecaseTest {

    @Mock private lateinit var mockPlayerRepository: PlayerRepository
    @Mock private lateinit var mockOk: (ExtractTeamsResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    private lateinit var subject: ExtractTeamsUsecase

    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var givenTeamNames: ExtractTeamsRequest

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
        givenTeamNames = ExtractTeamsRequest(teamString)
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
        verify(mockOk).invoke(ExtractTeamsResponse(expected))
    }

}