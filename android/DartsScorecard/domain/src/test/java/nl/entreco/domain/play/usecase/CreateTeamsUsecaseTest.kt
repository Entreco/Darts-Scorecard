package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.play.TestBackground
import nl.entreco.domain.play.TestForeground
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.model.players.TeamNamesString
import nl.entreco.domain.play.repository.PlayerRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class CreateTeamsUsecaseTest {

    @Mock private lateinit var mockPlayerRepository: PlayerRepository
    @Mock private lateinit var mockCallback: CreateTeamsUsecase.Callback
    private lateinit var subject: CreateTeamsUsecase

    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var givenTeamNames: TeamNamesString

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = CreateTeamsUsecase(mockPlayerRepository, bg, fg)
    }

    @Test
    fun start() {
        givenTeams("remco,piet|henk")
        whenStartingUsecase()
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
        subject.start(givenTeamNames, mockCallback)
    }

    private fun thenCallbackIsNotified(expected: String) {
        verify(mockCallback).onTeamsCreated(TeamIdsString(expected))
    }

}