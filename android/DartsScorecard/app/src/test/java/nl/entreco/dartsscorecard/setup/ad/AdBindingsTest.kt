package nl.entreco.dartsscorecard.setup.ad

import android.view.View
import com.google.android.gms.ads.AdListener
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.ad.AdBindings
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 29/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class AdBindingsTest {

    @Mock private lateinit var mockAdListener: AdListener
    @Mock private lateinit var mockView: View

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