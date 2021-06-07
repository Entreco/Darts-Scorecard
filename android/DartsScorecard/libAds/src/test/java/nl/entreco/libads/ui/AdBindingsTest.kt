package nl.entreco.libads.ui

import android.view.View
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock

class AdBindingsTest {

    private val mockAdListener: AdLoader.AdListener = mock()
    private val mockView: View = mock()

    @Ignore("Mockito cannot mock this class: class com.google.android.gms.ads.AdView.\n" +
            "Can not mock final classes with the following settings :\n" +
            " - explicit serialization (e.g. withSettings().serializable())\n" +
            " - extra interfaces (e.g. withSettings().extraInterfaces(...))\n")
    @Test
    fun `it should load add when true`() {
//        AdBindings.loadAd(mockAdView, true, mockAdListener)
    }

    @Ignore("Mockito cannot mock this class: class com.google.android.gms.ads.AdView.\n" +
            "Can not mock final classes with the following settings :\n" +
            " - explicit serialization (e.g. withSettings().serializable())\n" +
            " - extra interfaces (e.g. withSettings().extraInterfaces(...))\n")
    @Test
    fun `it should NOT load add when false`() {
//        AdBindings.loadAd(mockAdView, false, mockAdListener)
    }

    @Test
    fun `it should show add when true`() {
        AdBindings.showAd(mockView, true)
        verify(mockView).visibility = View.VISIBLE
    }

    @Test
    fun `it should NOT show add when false`() {
        AdBindings.showAd(mockView, false)
        verify(mockView).visibility = View.GONE
    }

}