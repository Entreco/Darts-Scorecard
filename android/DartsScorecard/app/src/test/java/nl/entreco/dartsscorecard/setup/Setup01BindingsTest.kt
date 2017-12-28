package nl.entreco.dartsscorecard.setup

import com.google.android.gms.ads.AdView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.exceptions.base.MockitoException

/**
 * Created by Entreco on 28/12/2017.
 */
class Setup01BindingsTest {

    @Mock private lateinit var mockAdView: AdView

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(expected = MockitoException::class)
    fun `it should request ad when true`() {
        Setup01Bindings.loadAdd(mockAdView, true)
        verify(mockAdView).loadAd(any())
    }

}