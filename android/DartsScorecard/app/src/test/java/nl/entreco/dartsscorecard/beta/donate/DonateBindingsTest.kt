package nl.entreco.dartsscorecard.beta.donate

import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.never
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
class DonateBindingsTest {

    @Mock private lateinit var mockViewGroup: ViewGroup

    @Test
    fun `it should clear previous views when list is NOT empty`() {
        whenever(mockViewGroup.childCount).thenReturn(5)
        DonateBindings.clearPreviousViewsIfEmpty(mockViewGroup)
        verify(mockViewGroup).removeViewAt(1)
        verify(mockViewGroup).removeViewAt(2)
        verify(mockViewGroup).removeViewAt(3)
        verify(mockViewGroup).removeViewAt(4)
    }

    @Test
    fun `it should remove empty View when list is empty`() {
        whenever(mockViewGroup.childCount).thenReturn(0)
        DonateBindings.clearPreviousViewsIfEmpty(mockViewGroup)
        verify(mockViewGroup, never()).removeViewAt(0)
    }

    @Test
    fun `it should remove empty View when list has 1 item`() {
        whenever(mockViewGroup.childCount).thenReturn(2)
        DonateBindings.clearPreviousViewsIfEmpty(mockViewGroup)
        verify(mockViewGroup).removeViewAt(1)
    }
}