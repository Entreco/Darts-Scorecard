package nl.entreco.dartsscorecard.play.live

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
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
class LiveStatBindingTest {

    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockLayoutParams: RelativeLayout.LayoutParams
    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockResources: Resources
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockPager: ViewPager
    @Mock private lateinit var mockAdapter: LiveStatAdapter

    @Test
    fun setupViewPager() {
        whenever(mockContext.resources).thenReturn(mockResources)
        whenever(mockPager.layoutParams).thenReturn(mockLayoutParams)
        whenever(mockPager.context).thenReturn(mockContext)
        LiveStatBinding.setupViewPager(mockPager, emptyMap(), mockAdapter)
        verify(mockPager).setPageTransformer(any(), any())
    }

    @Test
    fun `it should set Visibility to GONE if hide(true)`() {
        LiveStatBinding.hideIfOnlyOneTeam(mockView, true)
        verify(mockView).visibility = View.GONE
    }

    @Test
    fun `it should set Visibility to VISIBLE if hide(false)`() {
        LiveStatBinding.hideIfOnlyOneTeam(mockView, false)
        verify(mockView).visibility = View.VISIBLE
    }

    @Test
    fun `it should center text if oneTeam(true)`() {
        LiveStatBinding.centerIfOnlyOneTeam(mockTextView, true)
        verify(mockTextView).gravity = Gravity.CENTER
    }

    @Test
    fun `it should center | end if oneTeam(false)`() {
        LiveStatBinding.centerIfOnlyOneTeam(mockTextView, false)
        verify(mockTextView).gravity = Gravity.END
    }

    @Test
    fun `it should scroll to currentTeam`() {
        LiveStatBinding.scrollToCurrentTeam(mockPager, 0)
        verify(mockPager).setCurrentItem(0, true)
    }
}