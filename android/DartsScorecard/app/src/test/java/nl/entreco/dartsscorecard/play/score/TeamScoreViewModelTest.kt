package nl.entreco.dartsscorecard.play.score

import android.os.Handler
import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.finish.GetFinishRequest
import nl.entreco.domain.play.finish.GetFinishResponse
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.domain.play.listeners.events.NineDartEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
    private val givenTeam: Team = Team(arrayOf(playerFromMyTeam))
    private val givenStartScore: Score = Score()
    private lateinit var givenNext: Next
    private lateinit var givenScore: Score
    private lateinit var givenTurn: Turn
    @Mock private lateinit var mockHandler: Handler
    @Mock private lateinit var mockGetFinishUsecase: GetFinishUsecase
    private val finishRequestCaptor = argumentCaptor<GetFinishRequest>()
    private val finishResponseCaptor = argumentCaptor<(GetFinishResponse) -> Unit>()

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

    @Test
    fun `it should update 'scored' when player scores points`() {
        givenTeamScoreViewModel(true)
        whenScoringPoints(201)
        assertEquals(givenScore, subject.score.get())
    }

    @Test
    fun `it should start calculating finish when player scores points`() {
        givenTeamScoreViewModel(true)
        whenScoringPoints(201)
        thenScoreIs(givenScore.score)
    }

    @Test
    fun `it should remove 'scoringBadge'  when player scores points`() {
        givenTeamScoreViewModel(true)
        whenScoringPoints(201)
        verify(mockHandler).postDelayed(any(), eq(100))
    }

    @Test
    fun `it should update 'scored' when player in this team threw`() {
        givenTeamScoreViewModel(true)
        whenThrowing(Turn(), playerFromMyTeam)
        assertEquals(givenTurn.total(), subject.score.get().score - subject.scored.get())
    }

    @Test
    fun `it should calculate finish when player in this team threw`() {
        givenTeamScoreViewModel(true)
        whenThrowing(Turn(), playerFromMyTeam)
        thenScoreIs(501)
    }

    @Test
    fun `it should NOT update 'scored' when player in other team threw`() {
        givenTeamScoreViewModel(true)
        whenThrowing(Turn(Dart.SINGLE_1), playerFromOtherTeam)
        assertNotEquals(givenTurn.total(), subject.scored.get())
    }

    @Test
    fun `it should NOT calculate finish when player in other this team threw`() {
        givenTeamScoreViewModel(true)
        whenThrowing(Turn(Dart.DOUBLE_20), playerFromOtherTeam)
        verify(mockGetFinishUsecase, never()).calculate(any(), any())
    }

    @Test
    fun `it should set nine darter when nine darter possible for team && new leg is started`() {
        givenTeamScoreViewModel(false)
        whenNextEventOccurs(State.LEG, playerFromMyTeam)
        whenNineDartEvent(true, playerFromMyTeam)
        thenNineDarterIs(true)
    }

    @Test
    fun `it should set nine darter when nine darter possible and no non-ninedart event has occured`() {
        givenTeamScoreViewModel(false)
        whenNineDartEvent(true, playerFromMyTeam)
        thenNineDarterIs(true)
    }

    @Test
    fun `it should NOT set nine darter when nine darter possible but non-ninedart event has occured`() {
        givenTeamScoreViewModel(false)
        whenNineDartEvent(false, playerFromMyTeam)
        whenNineDartEvent(true, playerFromMyTeam)
        thenNineDarterIs(false)
    }

    @Test
    fun `it should NOT set nine darter, when not for this team`() {
        givenTeamScoreViewModel(false)
        whenNineDartEvent(true, playerFromOtherTeam)
        thenNineDarterIsNotUpdated()
    }

    @Test
    fun `it should set nine darter not possible, when not possible for this team`() {
        givenTeamScoreViewModel(false)
        whenNineDartEvent(false, playerFromMyTeam)
        thenNineDarterIs(false)
    }

    private fun whenLegFinishedByPlayer(player: Player) {
        whenNextEventOccurs(State.LEG, player)
    }

    private fun whenNormalTurnByPlayer(player: Player) {
        whenNextEventOccurs(State.NORMAL, player)
    }

    private fun givenTeamScoreViewModel(starter: Boolean) {
        subject = TeamScoreViewModel(givenTeam, givenStartScore, mockGetFinishUsecase, mockHandler, starter)
    }

    private fun whenNextEventOccurs(state: State, player: Player) {
        givenNext = Next(state, Team(arrayOf(Player("some team"))), 0, player, Score())
        subject.turnUpdate(givenNext)
    }

    private fun whenScoringPoints(score: Int) {
        givenScore = Score(score)
        subject.scored(givenScore, playerFromMyTeam)
    }

    private fun whenThrowing(turn: Turn, player: Player) {
        givenTurn = turn
        subject.threw(turn, player)
    }

    private fun whenNineDartEvent(nineDarter: Boolean, player: Player) {
        subject.onNineDartEvent(NineDartEvent(nineDarter, player))
    }

    private fun thenStartedFlagEquals(b: Boolean) {
        assertEquals(b, subject.started.get())
    }

    private fun andCurrentTeamIs(current: Boolean) {
        assertEquals(current, subject.currentTeam.get())
    }

    private fun thenScoreIs(score: Int) {
        verify(mockGetFinishUsecase).calculate(finishRequestCaptor.capture(), finishResponseCaptor.capture())
        assertEquals(score, finishRequestCaptor.lastValue.score.score)
    }

    private fun thenNineDarterIs(expected: Boolean) {
        assertEquals(expected, subject.nineDarter.get())
    }

    private fun thenNineDarterIsNotUpdated() {
        assertEquals(false, subject.nineDarter.get())
    }
}
