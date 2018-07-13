package nl.entreco.dartsscorecard.base

import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.Window
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.ActivityEditPlayerNameBinding
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 02/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class RevealAnimatorTest {

    @Mock private lateinit var mockWindow: Window
    @Mock private lateinit var mockTransition: Transition
    @Mock private lateinit var mockInflater: TransitionInflater
    @Mock private lateinit var mockView: View
    private lateinit var subject: RevealAnimator

    @Before
    fun setUp() {
        subject = RevealAnimator(mockView)
    }

    @Test
    fun setupEnterAnimation() {
        whenever(mockInflater.inflateTransition(any())).thenReturn(mockTransition)
        subject.setupEnterAnimation(mockInflater, mockWindow)
        verify(mockInflater).inflateTransition(R.transition.change_bound_with_arc)
    }

}
