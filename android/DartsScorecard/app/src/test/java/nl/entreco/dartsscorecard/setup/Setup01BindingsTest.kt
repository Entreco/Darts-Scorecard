package nl.entreco.dartsscorecard.setup

import com.google.android.gms.ads.AdView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 28/12/2017.
 */
class Setup01BindingsTest {

    @Mock private lateinit var mockAdView: AdView

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Ignore("Unable to mock AdView")
    @Test
    fun `it should request ad when true`() {
        Setup01Bindings.loadAdd(mockAdView, true)
        verify(mockAdView).loadAd(any())
    }

}