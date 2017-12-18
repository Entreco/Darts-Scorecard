package nl.entreco.dartsscorecard.play.score

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 18/12/2017.
 */
class ScoreAdapterTest {

    private val givenItems: MutableList<TeamScoreViewModel> = mutableListOf()
    private val subject = ScoreAdapter()

    @Mock private lateinit var mockViewHolder: TeamScoreView
    @Mock private lateinit var mockPlayer: Player
    @Mock private lateinit var mockScore: Score
    @Mock private lateinit var mockTurn: Turn
    @Mock private lateinit var mockNext: Next

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should return correct count initially`() {
        thenCountIs(0)
    }

    @Test
    fun `it should return correct count after adding items`() {
        givenItemsAdded(1, 2, 3, 4)
        thenCountIs(4)
    }

    @Test
    fun `it should clear items`() {
        givenItemsAdded(1, 2, 3, 4)
        whenClearIsCalled()
        thenCountIs(0)
    }

    @Test
    fun `it should bind to correct ViewHolder`() {
        givenItemsAdded(1, 2, 3, 4)
        whenBindingViewHolders()
        thenCountIs(4)
    }

    @Test
    fun `it should notify ViewHolders when team Scores`() {
        givenItemsAdded(0, 1, 2, 3)
        whenTeamAtPositionScores(0)
        thenViewHolderAtPositionIsNotifiedOfScore(0)
    }

    @Test
    fun `it should notify ViewHolders when team Threw`() {
        givenItemsAdded(0, 1, 2, 3)
        whenTeamAtPositionThrew(3)
        thenViewHolderAtPositionIsNotifiedOfThrow(3)
    }

    @Test
    fun `it should notify ViewHolders about Turn updates`() {
        givenItemsAdded(0, 1, 2, 3)
        whenTeamAtPositionReceivesTurnUpdate(2)
        thenViewHolderAtPositionIsNotifiedOfUpdate(2)
    }

    private fun givenItemsAdded(vararg items: Int) {
        items.forEach {
            val vm = mock<TeamScoreViewModel> {}
            givenItems.add(vm)
            subject.addItem(vm)
        }
    }

    private fun whenClearIsCalled() {
        subject.clear()
    }

    private fun whenBindingViewHolders() {
        givenItems.forEachIndexed { index, teamScoreViewModel ->
            subject.onBindViewHolder(mockViewHolder, index)
            verify(mockViewHolder).bind(teamScoreViewModel)
        }
    }

    private fun whenTeamAtPositionScores(position: Int) {
        subject.teamAtIndexScored(position, mockScore, mockPlayer)
    }

    private fun whenTeamAtPositionThrew(position: Int) {
        subject.teamAtIndexThrew(position, mockTurn, mockPlayer)
    }

    private fun whenTeamAtPositionReceivesTurnUpdate(position: Int) {
        subject.teamAtIndexTurnUpdate(position, mockNext)
    }

    private fun thenCountIs(expected: Int) {
        assertEquals(expected, subject.itemCount)
    }

    private fun thenViewHolderAtPositionIsNotifiedOfScore(position: Int) {
        verify(givenItems[position]).scored(mockScore, mockPlayer)
    }

    private fun thenViewHolderAtPositionIsNotifiedOfThrow(position: Int) {
        verify(givenItems[position]).threw(mockTurn, mockPlayer)
    }

    private fun thenViewHolderAtPositionIsNotifiedOfUpdate(position: Int) {
        verify(givenItems[position]).turnUpdate(mockNext)
    }
}