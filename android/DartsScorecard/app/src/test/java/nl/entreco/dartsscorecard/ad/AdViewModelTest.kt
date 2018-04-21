package nl.entreco.dartsscorecard.ad

import android.arch.lifecycle.Lifecycle
import nl.entreco.domain.Logger
import nl.entreco.domain.ad.FetchPurchasedItemsUsecase
import nl.entreco.domain.purchases.connect.ConnectToBillingUsecase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AdViewModelTest {

    @Mock private lateinit var mockBillingUsecas: ConnectToBillingUsecase
    @Mock private lateinit var mockFetchItemsUsecase: FetchPurchasedItemsUsecase
    @Mock private lateinit var mockAdLoader: AdLoader
    @Mock private lateinit var mockInterstitialLoader: InterstitialLoader
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockLifecycle: Lifecycle
    private lateinit var subject : AdViewModel

    @Test
    fun `it should register on init`() {
        givenSubject()
    }

    private fun givenSubject() {
        subject = AdViewModel(mockLifecycle, mockBillingUsecas, mockFetchItemsUsecase, mockAdLoader, mockInterstitialLoader, mockLogger)
    }
}