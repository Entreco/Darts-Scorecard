package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.listeners.StatListener
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ListenersTest {

    @Mock private lateinit var mockScoreListener: ScoreListener
    @Mock private lateinit var mockStatListener: StatListener
    @Mock private lateinit var mockSpecialEventListener: SpecialEventListener<*>
    @Mock private lateinit var mockPlayerListener: PlayerListener
    private lateinit var subject : Play01Listeners

    @Mock private lateinit var mockGame : Game
    @Mock private lateinit var mockNext : Next
    @Mock private lateinit var mockTurn : Turn
    @Mock private lateinit var mockPlayer : Player
    private lateinit var givenScores : Array<Score>
    @Mock private lateinit var teamScoreListener1 : TeamScoreListener
    @Mock private lateinit var teamScoreListener2 : TeamScoreListener

    @Test
    fun `it should register score listener`() {
        givenSubject()
        whenRegisteringListeners()
        thenScoreListenerIsAdded()
    }

    @Test
    fun `it should register special event listener`() {
        givenSubject()
        whenRegisteringListeners()
        thenSpecialEventListenerIsAdded()
    }

    @Test
    fun `it should register player listener`() {
        givenSubject()
        whenRegisteringListeners()
        thenPlayerListenersAreAdded()
    }

    @Test
    fun `it should add special event listeners when "Let's Play Darts"`() {
        givenSubject()
        whenLetsPlayDarts()
        thenTeamScoreListenersAreAdded()
    }

    @Test
    fun `it should notify player listeners when "Let's Play Darts"`() {
        givenSubject()
        whenRegisteringListeners()
        whenLetsPlayDarts()
        thenPlayerListenersAreNotified()
    }

    @Test
    fun `it should notify score listeners when "Let's Play Darts"`() {
        givenSubject()
        whenRegisteringListeners()
        whenLetsPlayDarts()
        thenScoreListenersAreNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify stat listeners when stats change`() {
        givenSubject()
        whenRegisteringListeners()
        whenStatsChange()
        thenStatListenersAreNotifiedOfDartThrown()
    }

    @Test
    fun `it should notify score listeners when dart thrown`() {
        givenSubject()
        whenRegisteringListeners()
        whenDartThrown()
        thenScoreListenersAreNotifiedOfDartThrown()
    }

    @Test
    fun `it should notify about special events when turn submitted`() {
        givenSubject()
        whenRegisteringListeners()
        whenTurnSubmitted()
        thenSpecialEventListenersAreNotified()
    }

    @Test
    fun `it should notify score listeners when turn submitted`() {
        givenSubject()
        whenRegisteringListeners()
        whenTurnSubmitted()
        thenScoreListenersAreNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify player listeners when turn submitted`() {
        givenSubject()
        whenRegisteringListeners()
        whenTurnSubmitted()
        thenPlayerListenersAreNotified()
    }

    private fun givenSubject() {
        subject = Play01Listeners()
    }

    private fun whenRegisteringListeners() {
        subject.registerListeners(mockScoreListener, mockStatListener, mockSpecialEventListener, mockPlayerListener, mockPlayerListener)
    }

    private fun whenLetsPlayDarts() {
        givenScores = arrayOf(Score(), Score())
        whenever(mockNext.player).thenReturn(mockPlayer)
        whenever(mockGame.next).thenReturn(mockNext)
        whenever(mockGame.scores).thenReturn(givenScores)
        subject.onLetsPlayDarts(mockGame, listOf(teamScoreListener1, teamScoreListener2))
    }

    private fun whenStatsChange() {
        subject.onStatsUpdated(34, 44)
    }

    private fun whenDartThrown() {
        subject.onDartThrown(mockTurn, mockPlayer)
    }

    private fun whenTurnSubmitted() {
        givenScores = arrayOf(Score(), Score())
        subject.onTurnSubmitted(mockNext, mockTurn, mockPlayer, givenScores)
    }

    private fun thenScoreListenerIsAdded() {
        assertEquals(1, subject.scoreListeners.size)
        assertEquals(mockScoreListener, subject.scoreListeners[0])
    }

    private fun thenSpecialEventListenerIsAdded() {
        assertEquals(1, subject.specialEventListeners.size)
        assertEquals(mockSpecialEventListener, subject.specialEventListeners[0])
    }

    private fun thenPlayerListenersAreAdded() {
        assertEquals(1, subject.playerListeners.size)
        assertEquals(mockPlayerListener, subject.playerListeners[0])
    }

    private fun thenTeamScoreListenersAreAdded() {
        assertEquals(2, subject.specialEventListeners.size)
        assertEquals(teamScoreListener1, subject.specialEventListeners[0])
        assertEquals(teamScoreListener2, subject.specialEventListeners[1])
    }

    private fun thenPlayerListenersAreNotified() {
        verify(mockPlayerListener).onNext(mockNext)
    }

    private fun thenScoreListenersAreNotifiedOfScoreChange() {
        verify(mockScoreListener).onScoreChange(any(), any())
    }

    private fun thenScoreListenersAreNotifiedOfDartThrown() {
        verify(mockScoreListener).onDartThrown(any(), any())
    }
    
    private fun thenStatListenersAreNotifiedOfDartThrown() {
        verify(mockStatListener).onStatsChange(any(), any())
    }

    private fun thenSpecialEventListenersAreNotified() {
        verify(mockSpecialEventListener).onSpecialEvent(mockNext, mockTurn, mockPlayer, givenScores)
    }
}
