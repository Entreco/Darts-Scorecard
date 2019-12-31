package nl.entreco.libconsent.ask

import android.content.Context
import android.view.WindowManager
import com.google.ads.consent.ConsentForm
import com.google.ads.consent.ConsentFormListener
import com.google.ads.consent.ConsentStatus
import nl.entreco.libconsent.ConsentPrefs
import java.net.URL
import javax.inject.Inject

class AskConsentUsecase @Inject constructor(
        private val prefs: ConsentPrefs
) {
    private var form: ConsentForm? = null
    private var onSelected: (AskConsentResponse) -> Unit = {}
    private val consentFormListener = object : ConsentFormListener() {
        override fun onConsentFormClosed(consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
            super.onConsentFormClosed(consentStatus, userPrefersAdFree)
            prefs.store(consentStatus?.name, null)
            val response = when {
                userPrefersAdFree == true                       -> AskConsentResponse.paid()
                consentStatus == ConsentStatus.PERSONALIZED     -> AskConsentResponse.normal()
                consentStatus == ConsentStatus.NON_PERSONALIZED -> AskConsentResponse.npa()
                else                                            -> null
            }

            response?.let {
                onSelected(it)
            }
        }

        override fun onConsentFormLoaded() {
            try {
                form?.show()
            } catch (badWindowToken: WindowManager.BadTokenException){
                // Shame on you, for catching this exception and silently ignoring it ;(
            }
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