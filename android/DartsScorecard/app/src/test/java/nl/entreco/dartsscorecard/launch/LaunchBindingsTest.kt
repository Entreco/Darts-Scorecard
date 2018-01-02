package nl.entreco.dartsscorecard.launch

import android.view.View
import android.view.ViewPropertyAnimator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LaunchBindingsTest {

    @Mock private lateinit var mockView : View
    @Mock private lateinit var mockAnimator : ViewPropertyAnimator
    private val givenRequest = RetrieveGameRequest(33, TeamIdsString("1|2"), CreateGameRequest(1,2,3,4))

    @Before
    fun setUp() {
        whenever(mockView.animate()).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
    }

    @Test
    fun `it should fade in when non null`() {
        LaunchBindings.resumeGame(mockView, givenRequest)
        verify(mockView).animate()
        verify(mockAnimator).alpha(1F)
        verify(mockAnimator).start()
    }

    @Test
    fun `it should fade out when null`() {
        LaunchBindings.resumeGame(mockView, null)
        verify(mockView).animate()
        verify(mockAnimator).alpha(0.5F)
        verify(mockAnimator).start()
    }

}