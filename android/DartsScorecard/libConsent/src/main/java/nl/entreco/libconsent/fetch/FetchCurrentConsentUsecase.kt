package nl.entreco.libconsent.fetch

import android.content.SharedPreferences
import com.google.ads.consent.ConsentStatus
import nl.entreco.libconsent.Consent
import javax.inject.Inject

class FetchCurrentConsentUsecase @Inject constructor(
        private val prefs: SharedPreferences
) {
    private val unknown = ConsentStatus.UNKNOWN.toString()
    private val nonPersonalized = ConsentStatus.NON_PERSONALIZED.toString()

    fun go(done: (FetchConsentResponse) -> Unit) {
        val fromEu = prefs.getBoolean(Consent.Eu, false)
        val givenConsent = prefs.getString(Consent.Status, unknown)
        done(FetchConsentResponse(fromEu && givenConsent == unknown, givenConsent == nonPersonalized))
    }
}