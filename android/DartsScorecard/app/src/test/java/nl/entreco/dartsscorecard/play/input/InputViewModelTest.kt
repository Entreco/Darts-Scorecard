package nl.entreco.dartsscorecard.play.input

import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.NoPlayer
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.InputListener
import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.SpecialEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 09/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class InputViewModelTest {
    @InjectMocks private lateinit var subject: InputViewModel

    @Mock private lateinit var mockListener: InputListener
    @Mock private lateinit var mockInput: TextView

    private lateinit var givenEvent: SpecialEvent
    private lateinit var givenPlayer: Player
    private lateinit var givenRequiredScore: Score

    @Test
    fun `it should put toggle in 'off' state initially`() {
        assertFalse(subject.toggle.get())
    }

    @Test
    fun `it should have NoPlayer as initial player`() {
        assertEquals(NoPlayer().toString(), subject.current.get().toString())
    }

    @Test
    fun `it should have empty scoredTxt initially`() {
        assertEquals("", subject.scoredTxt.get())
    }

    @Test
    fun `it should have empty description of the next player`() {
        assertEquals(R.string.empty, subject.nextDescription.get())
    }

    @Test
    fun `it should have empty specialEvent initially`() {
        assertEquals(null, subject.special.get())
    }

    @Test
    fun `it should set NoScoreEvent if NoScore occurred`() {
        givenNoScoreEvent()
        assertEquals(givenEvent, subject.special.get())
    }

    @Test
    fun `it should set null if non-NoScore occurred`() {
        givenNonNoScoreEvent()
        assertEquals(null, subject.special.get())
    }

    @Test
    fun `it should pass BustEvent to special ObservableField`() {
        givenBustEvent()
        assertEquals(givenEvent, subject.special.get())
    }

    @Test
    fun `it should show entered text in scoredTxt`() {
        givenEntered(181)
        thenScoredTxtIs("181")
    }

    @Test
    fun `it should add a maximum of 3 digits to scoredTxt`() {
        givenEntered(0)
        thenScoredTxtIs("0")
        givenEntered(1)
        thenScoredTxtIs("01")
        givenEntered(2)
        thenScoredTxtIs("012")
        givenEntered(3)
        thenScoredTxtIs("012")
    }

    @Test
    fun `it should remove a character on back`() {
        givenEntered(181)
        whenPressingBack()
        thenScoredTxtIs("18")
    }

    @Test
    fun `it should clear scoredTxt on Long press`() {
        givenEntered(66)
        whenLongPressingBack()
        thenScoredTxtIs("")
    }

    @Test
    fun `it should NOT submit Turn when hint pressed, but no player is throwing`() {
        whenPressingHint(0)
        verify(mockListener, never()).onTurnSubmitted(any(), any())
    }

    @Test
    fun `it should submit Turn when hint pressed`() {
        givenPlayer("player1")
        whenPressingHint(0)
        verify(mockListener).onTurnSubmitted(any(), eq(givenPlayer))
    }

    @Test
    fun `it should NOT submit Turn when hint pressed, with single mode`() {
        givenPlayer("player1")
        givenSingleMode(true)
        whenPressingHint(0)
        verify(mockListener, never()).onTurnSubmitted(any(), eq(givenPlayer))
    }

    @Test
    fun `it should submit Dart when hint pressed, with single mode`() {
        givenPlayer("player1")
        givenSingleMode(true)
        whenPressingHint(1)
        verify(mockListener).onDartThrown(any(), eq(givenPlayer))
    }

    @Test
    fun `it should submit Bust when hint pressed`() {
        givenPlayer("player1")
        whenPressingHint(-1)
        verify(mockListener).onTurnSubmitted(any(), eq(givenPlayer))
    }


    @Test
    fun `it should submit Bust when hint pressed in single Mode`() {
        givenPlayer("player1")
        givenSingleMode(true)
        whenPressingHint(-1)
        verify(mockListener).onTurnSubmitted(any(), eq(givenPlayer))
    }

    @Test
    fun `it should submit Darts when 'throw' is pressed`() {
        givenPlayer("player1")
        whenPressingSubmit(140)
        verify(mockListener).onTurnSubmitted(any(), eq(givenPlayer))
    }

    @Test
    fun `it should submit Turn() = (with score 0) when 'invalid throw (eg unparsable score)' is entered`() {
        givenPlayer("player1")
        whenPressingSubmit("this is not a valid sore -> it's a string dude")
        verify(mockListener).onTurnSubmitted(isA(), eq(givenPlayer))
    }

    @Test
    fun `it should submit Single Darts when 'first throw' is submitted`() {
        givenPlayer("player1")
        whenSubmittingSingle(40)
        verify(mockListener).onDartThrown(any(), eq(givenPlayer))
    }

    @Test
    fun `it should submit 2 x Single Darts when 'second throw' is submitted`() {
        givenPlayer("player1")
        whenSubmittingSingle(40)
        whenSubmittingSingle(60)
        verify(mockListener, times(2)).onDartThrown(any(), eq(givenPlayer))
    }

    @Test
    fun `it should submit Darts when 'third throw' is submitted`() {
        givenPlayer("player1")
        whenSubmittingSingle(40)
        whenSubmittingSingle(60)
        whenSubmittingSingle(20)
        verify(mockListener, times(3)).onDartThrown(any(), eq(givenPlayer))
        verify(mockListener).onTurnSubmitted(any(), eq(givenPlayer))
    }

    @Test
    fun `it should reset 'toggle' state, when Next player is throwing`() {
        givenPlayer("player1")
        whenSubmittingSingle(40)
        thenToggleState(true)
        givenPlayer("other player")
        thenToggleState(false)
    }

    @Test
    fun `it should store required Score, when Next player is throwing`() {
        givenPlayer("some player", 201)
        thenRequiredIs(201)
    }

    @Test
    fun `it should submit Score when finishing with 1 dart in SingleMode`() {
        givenPlayer("Mvg", 18)
        givenSingleMode(true)
        whenSubmittingSingle(18)
        verify(mockListener).onDartThrown(any(), eq(givenPlayer))
        verify(mockListener).onTurnSubmitted(Turn(Dart.DOUBLE_9), givenPlayer)
    }

    @Test
    fun `it should submit Score when finishing with 2 darts in SingleMode`() {
        givenPlayer("Mvg", 18)
        givenSingleMode(true)
        whenSubmittingSingle(2)
        whenSubmittingSingle(16)
        verify(mockListener, times(2)).onDartThrown(any(), eq(givenPlayer))
        verify(mockListener).onTurnSubmitted(Turn(Dart.DOUBLE_1, Dart.DOUBLE_8), givenPlayer)
    }

    @Test
    fun `it should show correct next state (normal)`() {
        givenPlayer("1", 201, State.NORMAL)
        assertEquals(R.string.to_throw, subject.nextDescription.get())
    }

    @Test
    fun `it should show correct next state (start)`() {
        givenPlayer("1", 201, State.START)
        assertEquals("should be: 'game on'", R.string.game_on, subject.nextDescription.get())
    }

    @Test
    fun `it should show correct next state (match)`() {
        givenPlayer("1", 201, State.MATCH)
        assertEquals("should be: 'game shot and the match'", R.string.game_shot_and_match, subject.nextDescription.get())
    }

    @Test
    fun `it should show correct next state (set)`() {
        givenPlayer("1", 201, State.SET)
        assertEquals("should be: 'to throw first'", R.string.to_throw_first, subject.nextDescription.get())
    }

    @Test
    fun `it should show correct next state (leg)`() {
        givenPlayer("1", 201, State.LEG)
        assertEquals("should be: 'to throw first'", R.string.to_throw_first, subject.nextDescription.get())
    }

    private fun givenPlayer(playerName: String, pts: Int = 501, state: State = State.NORMAL) {
        givenPlayer = Player(playerName)
        givenRequiredScore = Score(pts, 0, 0)
        subject.onNext(Next(state, Team(arrayOf(givenPlayer)), 0, givenPlayer, givenRequiredScore))
    }

    private fun givenEntered(scored: Int) {
        subject.entered(scored)
    }

    private fun givenBustEvent() {
        givenEvent = BustEvent()
        subject.onBustEvent(givenEvent as BustEvent)
    }

    private fun givenNoScoreEvent() {
        givenEvent = NoScoreEvent(true)
        subject.onNoScoreEvent(givenEvent as NoScoreEvent)
    }

    private fun givenNonNoScoreEvent() {
        givenEvent = NoScoreEvent(false)
        subject.onNoScoreEvent(givenEvent as NoScoreEvent)
    }

    private fun givenSingleMode(singleMode: Boolean) {
        subject.toggle.set(singleMode)
    }

    private fun whenPressingBack() {
        subject.back()
    }

    private fun whenLongPressingBack() {
        subject.clear()
    }

    private fun whenPressingHint(hintDigit: Int) {
        subject.onPressedKey(hintDigit, mockListener)
    }

    private fun whenPressingSubmit(scored: Int) {
        whenever(mockInput.text).thenReturn(scored.toString())
        subject.onSubmitScore(mockInput, mockListener)
    }

    private fun whenPressingSubmit(scored: String) {
        whenever(mockInput.text).thenReturn(scored)
        subject.onSubmitScore(mockInput, mockListener)
    }

    private fun whenSubmittingSingle(scored: Int) {
        givenSingleMode(true)
        whenPressingSubmit(scored)
    }

    private fun thenScoredTxtIs(expected: String) {
        assertEquals(expected, subject.scoredTxt.get())
    }

    private fun thenToggleState(state: Boolean) {
        assertEquals(state, subject.toggle.get())
    }

    private fun thenRequiredIs(required: Int) {
        assertEquals(required, subject.required.get().score)
    }
}
