package nl.entreco.libconsent.ask

import android.content.Context
import com.google.ads.consent.ConsentForm
import com.google.ads.consent.ConsentFormListener
import com.google.ads.consent.ConsentStatus
import java.net.URL
import javax.inject.Inject

class AskConsentUsecase @Inject constructor() {
    private var form: ConsentForm? = null
    private val consentFormListener = object : ConsentFormListener() {
        override fun onConsentFormClosed(consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
            super.onConsentFormClosed(consentStatus, userPrefersAdFree)
        }

        override fun onConsentFormError(reason: String?) {
            super.onConsentFormError(reason)
        }

        override fun onConsentFormLoaded() {
            form?.show()
        }

        override fun onConsentFormOpened() {
            super.onConsentFormOpened()
        }
    }

    fun askForConsent(context: Context) {
        form = ConsentForm.Builder(context, URL("https://dsc.entreco.nl/privacy-policy.html"))
                .withListener(consentFormListener)
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption().build()
        form?.load()
    }
}