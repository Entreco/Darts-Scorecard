package nl.entreco.dartsscorecard.play

import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.dartsscorecard.R
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 25/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01BindingTest {

    @Mock private lateinit var mockCoordinator: CoordinatorLayout
    @Mock private lateinit var mockAnimator: Play01Animator
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockView: View

    @Test(expected = NullPointerException::class)
    fun `it should add loading view`() {
        whenever(mockCoordinator.context).thenReturn(mockContext)
        Play01Binding.showLoading(mockCoordinator, true)
        verify(mockCoordinator).addView(any())
    }

    @Test
    fun `it should collapse when finished`() {
        Play01Binding.showGameFinished(mockCoordinator, true, mockAnimator)
        verify(mockAnimator).collapse()
    }

    @Test(expected = NullPointerException::class) // Indicating Snackbar is shown
    fun `it should show snack (with message)`() {
        Play01Binding.showSnack(mockView, R.string.app_name)
    }

    @Test
    fun `it should NOT show snack (empty)`() {
        Play01Binding.showSnack(mockView, 0)
    }

    @Test
    fun `it should NOT collapse when not finished`() {
        Play01Binding.showGameFinished(mockCoordinator, false, mockAnimator)
        verify(mockAnimator, never()).collapse()
    }
}