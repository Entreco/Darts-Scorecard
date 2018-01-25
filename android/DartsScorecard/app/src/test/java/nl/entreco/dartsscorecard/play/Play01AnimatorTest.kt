package nl.entreco.dartsscorecard.play

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 25/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01AnimatorTest{

    @Mock private lateinit var mockCoordinator : CoordinatorLayout
    @Mock private lateinit var mockContext : Context
    
    @Test(expected = NullPointerException::class)
    fun `it should add loading view`() {
        whenever(mockCoordinator.context).thenReturn(mockContext)
        Play01Animator.showLoading(mockCoordinator, true)
        verify(mockCoordinator).addView(any())
    }
}