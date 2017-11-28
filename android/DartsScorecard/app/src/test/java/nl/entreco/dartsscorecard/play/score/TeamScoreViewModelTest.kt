package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.GetFinishUsecase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 28/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamScoreViewModelTest {

    private lateinit var subject: TeamScoreViewModel

    private val playerFromMyTeam: Player = Player("pietje")
    private val playerFromOtherTeam: Player = Player("paultje")
    private val givenTeam: Team = Team(playerFromMyTeam)
    private val givenStartScore: Score = Score()
    private lateinit var givenNext: Next
    @Mock private lateinit var mockGetFinishUsecase: GetFinishUsecase

    @Test
    fun `when leg finished by other team, it should be false`() {
        givenTeamScoreViewModel(true)
        whenLegFinishedByPlayer(playerFromOtherTeam)
        thenStartedFlagEquals(false)
        andCurrentTeamIs(false)
    }

    @Test
    fun `when normal turn from other team, it should stay true`() {
        givenTeamScoreViewModel(true)
        whenNormalTurnByPlayer(playerFromOtherTeam)
        thenStartedFlagEquals(true)
        andCurrentTeamIs(false)
    }

    @Test
    fun `when leg finished by my team, it should be true`() {
        givenTeamScoreViewModel(true)
        whenLegFinishedByPlayer(playerFromMyTeam)
        thenStartedFlagEquals(true)
        andCurrentTeamIs(true)
    }

    @Test
    fun `when normal turn from my team, it should stay true`() {
        givenTeamScoreViewModel(true)
        whenNormalTurnByPlayer(playerFromMyTeam)
        thenStartedFlagEquals(true)
        andCurrentTeamIs(true)
    }

    private fun whenLegFinishedByPlayer(player: Player) {
        whenNextEventOccurs(State.LEG, player)
    }

    private fun whenNormalTurnByPlayer(player: Player) {
        whenNextEventOccurs(State.NORMAL, player)
    }

    private fun givenTeamScoreViewModel(starter: Boolean) {
        subject = TeamScoreViewModel(givenTeam, givenStartScore, mockGetFinishUsecase, starter)
    }

    private fun whenNextEventOccurs(state: State, player: Player) {
        givenNext = Next(state, Team("some team"), 0, player)
        subject.turnUpdate(givenNext)
    }

    private fun thenStartedFlagEquals(b: Boolean) {
        assertEquals(b, subject.started.get())
    }

    private fun andCurrentTeamIs(current: Boolean) {
        assertEquals(current, subject.currentTeam.get())
    }
}