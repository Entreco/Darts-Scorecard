package nl.entreco.dartsscorecard.beta.donate

import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 08/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class DonateBindingsTest{

    @Mock private lateinit var mockViewGroup : ViewGroup

    @Test
    fun `it should clear previous views`() {
        whenever(mockViewGroup.childCount).thenReturn(5)
        DonateBindings.clearPreviousViews(mockViewGroup)
        verify(mockViewGroup).removeViewAt(1)
        verify(mockViewGroup).removeViewAt(2)
        verify(mockViewGroup).removeViewAt(3)
        verify(mockViewGroup).removeViewAt(4)
    }
}