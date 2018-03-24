package nl.entreco.dartsscorecard.play.stats

import android.support.v4.view.ViewPager
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 24/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MatchStatBindingTest {

    @Mock private lateinit var mockPager: ViewPager
    @Mock private lateinit var mockAdapter: MatchStatAdapter

    @Test
    fun setupViewPager() {
        MatchStatBinding.setupViewPager(mockPager, emptyMap(), mockAdapter)
    }

}