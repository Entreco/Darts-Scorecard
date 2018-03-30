package nl.entreco.dartsscorecard.base.widget

import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 24/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class WrapContentViewPagerTest {

    @Mock private lateinit var mockContext: Context
    private lateinit var subject : WrapContentViewPager

    @Test(expected = NullPointerException::class)
    fun onMeasure() {
        givenSubject()
    }


    private fun givenSubject() {
        subject = WrapContentViewPager(mockContext, null)
    }

}