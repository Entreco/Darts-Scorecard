package nl.entreco.dartsscorecard.play.score

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import nl.entreco.domain.Logger
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
import nl.entreco.domain.setup.game.CreateGameRequest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 28/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class ScoreViewModelTest {

    private lateinit var subject: ScoreViewModel
    private val player1 = Player("1")
    private val player2 = Player("2")
    private val player3 = Player("3")

    private val givenTeams = arrayOf(Team(arrayOf(player1)), Team(arrayOf(player2)), Team(arrayOf(player3)))
    private val givenScores = arrayOf(Score(), Score(), Score())
    private val givenScoreSettings = ScoreSettings()
    private lateinit var givenTurn: Turn
    private lateinit var givenNext: Next
    @Mock private lateinit var mockAdapter: ScoreAdapter
    @Mock private lateinit var mockPlayer: Player
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockCallback: UiCallback

    @Before
    fun setUp() {
        subject = ScoreViewModel(mockAdapter, mockLogger)
    }

    @Test
    fun `it should get numTeams from ScoreSettings`() {
        assertEquals(0, subject.numSets.get())
    }

    @Test
    fun `it should notify adapter for each score about scoreUpdates`() {
        `when score changed`()
        `then adapter will notify each team about it`()
    }

    @Test
    fun `it should notify adapter which player of which team threw some darts`() {
        `given 3 Darts thrown`(Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3)
        `when a this player throws`(player2)
        `then Adapter is notified that TeamAtIndex has thrown`(-1, player2)
    }

    @Test
    fun `it should notify adapter for each team, about turn updates, when game started`() {
        `given game has started`()
        `given NextInfo for Team`(0)
        `when onNext() is called`()
        `then Adapter should notify each Team about next`()
    }

    @Test
    fun `it should NOT notify adapter about turn updates, when game not started`() {
        `given NextInfo for Team`(0)
        `when onNext() is called`()
        `then Adapter should NOT notify each Team about next`()
    }

    @Test
    fun `it should set UiCallback in Observable when game started`() {
        `given game has started`()
        assertEquals(mockCallback, subject.uiCallback.get())
    }

    @Test
    fun `it should set ScoreSettings in Observable when game started`() {
        `given game has started`()
        assertEquals(givenScoreSettings, subject.scoreSettings.get())
    }

    @Test
    fun `it should add Teams to ObservableList when game started`() {
        `given game has started`()
        assertEquals(givenTeams.size, subject.teams.size)
    }

    @Test
    fun `it should set numSets in Observable when game started`() {
        `given game has started`()
        assertEquals(givenScoreSettings.numSets, subject.numSets.get())
    }

    private fun `given game has started`() {
        subject.onLoaded(givenTeams, givenScores, CreateGameRequest(givenScoreSettings.startScore, 0, givenScoreSettings.numLegs, givenScoreSettings.numSets), mockCallback)
    }

    private fun `given NextInfo for Team`(index: Int) {
        givenNext = Next(State.MATCH, givenTeams[index], index, givenTeams[index].players[0], Score())
    }

    private fun `given 3 Darts thrown`(d1: Dart, d2: Dart, d3: Dart) {
        givenTurn = Turn(d1, d2, d3)
    }

    private fun `when score changed`() {
        subject.onScoreChange(givenScores, mockPlayer)
    }

    private fun `when a this player throws`(player: Player) {
        subject.onDartThrown(givenTurn, player)
    }

    private fun `when onNext() is called`() {
        subject.onNext(givenNext)
    }

    private fun `then adapter will notify each team about it`() {
        givenTeams.forEachIndexed { index, _ -> verify(mockAdapter).teamAtIndexScored(index, givenScores[index], mockPlayer) }
    }

    private fun `then Adapter is notified that TeamAtIndex has thrown`(teamIndex: Int, player: Player) {
        verify(mockAdapter).teamAtIndexThrew(teamIndex, givenTurn, player)
    }

    private fun `then Adapter should notify each Team about next`() {
        givenTeams.forEachIndexed { index, _ -> verify(mockAdapter).teamAtIndexTurnUpdate(index, givenNext) }
    }

    private fun `then Adapter should NOT notify each Team about next`() {
        verifyZeroInteractions(mockAdapter)
    }

}