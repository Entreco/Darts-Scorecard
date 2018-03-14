package nl.entreco.dartsscorecard.setup.players

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayersObserverTest {

    @Mock private lateinit var mockCallback: PlayersObserver.Callback
    private lateinit var subject: PlayersObserver

    @Test
    fun `it should notify callback onItemRangeInserted`() {
        givenSubject()
        whenItemRangeInserted(1, 1)
        thenCallbackIsNotified(1, 1)
    }

    @Test
    fun `it should NOT notify callback onChanged`() {
        givenSubject()
        whenOnChanged()
        thenCallbackIsNotNotified()
    }

    @Test
    fun `it should NOT notify callback onItemRangeChanged`() {
        givenSubject()
        whenItemRangeChanged()
        thenCallbackIsNotNotified()
    }

    @Test
    fun `it should NOT notify callback onItemRangeMoved`() {
        givenSubject()
        whenItemRangeMoved()
        thenCallbackIsNotNotified()
    }

    @Test
    fun `it should NOT notify callback onItemRangeRemoved`() {
        givenSubject()
        whenItemRangeRemoved()
        thenCallbackIsNotNotified()
    }

    private fun givenSubject() {
        subject = PlayersObserver(mockCallback)
    }

    private fun whenItemRangeInserted(start: Int, count: Int) {
        subject.onItemRangeInserted(start, count)
    }

    private fun whenItemRangeChanged() {
        subject.onItemRangeChanged(2, 4)
    }

    private fun whenItemRangeMoved() {
        subject.onItemRangeMoved(2, 4, 3)
    }


    private fun whenItemRangeRemoved() {
        subject.onItemRangeRemoved(2, 4)
    }

    private fun whenOnChanged() {
        subject.onChanged()
    }

    private fun thenCallbackIsNotified(start: Int, count: Int) {
        verify(mockCallback).onPlayersInserted(start, count)
    }

    private fun thenCallbackIsNotNotified() {
        verifyZeroInteractions(mockCallback)
    }

}