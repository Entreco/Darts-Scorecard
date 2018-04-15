package nl.entreco.dartsscorecard.play.stats

import android.content.Context
import android.content.res.Resources
import android.support.v4.view.ViewPager
import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 24/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MatchStatBindingTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockResources: Resources
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockPager: ViewPager
    @Mock private lateinit var mockAdapter: MatchStatAdapter

    @Test
    fun setupViewPager() {
        whenever(mockContext.resources).thenReturn(mockResources)
        whenever(mockPager.context).thenReturn(mockContext)
        MatchStatBinding.setupViewPager(mockPager, emptyMap(), mockAdapter)
        verify(mockPager).setPageTransformer(any(), any())
    }

    @Test
    fun `it should set Visibility to GONE if hide(true)`() {
        MatchStatBinding.hideIfOnlyOneTeam(mockView, true)
        verify(mockView).visibility = View.GONE
    }

    @Test
    fun `it should set Visibility to VISIBLE if hide(false)`() {
        MatchStatBinding.hideIfOnlyOneTeam(mockView, false)
        verify(mockView).visibility = View.VISIBLE
    }
}