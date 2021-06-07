package nl.entreco.dartsscorecard.beta.donate

import android.view.ViewGroup
import org.mockito.kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 08/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class DonateBindingsTest {

    @Mock private lateinit var mockViewGroup: ViewGroup

    @Test
    fun `it should clear previous views`() {
        DonateBindings.clearPreviousViewsIfEmpty(mockViewGroup)
        verify(mockViewGroup).removeAllViews()
    }
}