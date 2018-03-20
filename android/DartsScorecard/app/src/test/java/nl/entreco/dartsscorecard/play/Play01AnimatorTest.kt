package nl.entreco.dartsscorecard.play

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.R
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 25/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01AnimatorTest {

    @Mock private lateinit var mockCoordinator: CoordinatorLayout
    @Mock private lateinit var mockAnimator: Play01Animator
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockView: View

    @Test(expected = NullPointerException::class)
    fun `it should add loading view`() {
        whenever(mockCoordinator.context).thenReturn(mockContext)
        Play01Animator.showLoading(mockCoordinator, true)
        verify(mockCoordinator).addView(any())
    }

    @Test
    fun `it should collapse when finished`() {
        Play01Animator.showGameFinished(mockCoordinator, true, mockAnimator)
        verify(mockAnimator).collapse()
    }

    @Test(expected = NullPointerException::class) // Indicating Snackbar is shown
    fun `it should show snack (with message)`() {
        Play01Animator.showSnack(mockView, R.string.app_name)
    }

    @Test
    fun `it should NOT show snack (empty)`() {
        Play01Animator.showSnack(mockView, 0)
    }

    @Test
    fun `it should NOT collapse when not finished`() {
        Play01Animator.showGameFinished(mockCoordinator, false, mockAnimator)
        verify(mockAnimator, never()).collapse()
    }
}