package nl.entreco.libconsent.ask

import android.content.Context
import com.google.ads.consent.ConsentForm
import com.google.ads.consent.ConsentFormListener
import com.google.ads.consent.ConsentStatus
import nl.entreco.libconsent.store.StoreCurrentConsentUsecase
import java.net.URL
import javax.inject.Inject

class AskConsentUsecase @Inject constructor(
        private val storeCurrentConsentUsecase: StoreCurrentConsentUsecase
) {
    private var form: ConsentForm? = null
    private var onSelected: (AskConsentResponse) -> Unit = {}
    private val consentFormListener = object : ConsentFormListener() {
        override fun onConsentFormClosed(consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
            super.onConsentFormClosed(consentStatus, userPrefersAdFree)
            storeCurrentConsentUsecase.go(consentStatus?.name)
            val response = when {
                userPrefersAdFree == true                       -> AskConsentResponse.paid()
                consentStatus == ConsentStatus.PERSONALIZED     -> AskConsentResponse.normal()
                consentStatus == ConsentStatus.NON_PERSONALIZED -> AskConsentResponse.npa()
                else                                            -> null
            }

            response?.let{
                onSelected(it)
            }
        }

        override fun onConsentFormLoaded() {
            form?.show()
        }
    }

    fun askForConsent(context: Context, done: (AskConsentResponse) -> Unit) {
        onSelected = done
        form = ConsentForm.Builder(context, URL("https://dsc.entreco.nl/privacy-policy.html"))
                .withListener(consentFormListener)
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption().build()
        form?.load()
    }
}