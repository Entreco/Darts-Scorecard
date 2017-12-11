package nl.entreco.dartsscorecard.play.score

import android.os.Handler
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.GetFinishUsecase
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
    private val givenTeam: Team = Team(playerFromMyTeam)
    private val givenStartScore: Score = Score()
    private lateinit var givenNext: Next
    private lateinit var givenScore: Score
    private lateinit var givenTurn: Turn
    @Mock private lateinit var mockHandler: Handler
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
        verify(mockGetFinishUsecase).calculate(eq(givenScore), any(), any(), any())
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
        assertEquals(givenTurn.total(), subject.scored.get())
    }

    @Test
    fun `it should calculate finish when player in this team threw`() {
        givenTeamScoreViewModel(true)
        whenThrowing(Turn(), playerFromMyTeam)
        verify(mockGetFinishUsecase).calculate(any(), eq(givenTurn), any(), any())
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
        verify(mockGetFinishUsecase, never()).calculate(any(), eq(givenTurn), any(), any())
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
        givenNext = Next(state, Team("some team"), 0, player, Score())
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

    private fun thenStartedFlagEquals(b: Boolean) {
        assertEquals(b, subject.started.get())
    }

    private fun andCurrentTeamIs(current: Boolean) {
        assertEquals(current, subject.currentTeam.get())
    }
}