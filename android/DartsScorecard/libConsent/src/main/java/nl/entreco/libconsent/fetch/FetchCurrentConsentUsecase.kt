package nl.entreco.libconsent.fetch

import com.google.ads.consent.ConsentStatus
import nl.entreco.libconsent.ConsentPrefs
import javax.inject.Inject

class FetchCurrentConsentUsecase @Inject constructor(
        private val prefs: ConsentPrefs
) {

    private val unknown = ConsentStatus.UNKNOWN.toString()
    private val nonPersonalized = ConsentStatus.NON_PERSONALIZED.toString()

    fun go(done: (FetchConsentResponse) -> Unit) {
        val (fromEu, givenConsent) = prefs.fetch(unknown)
        done(FetchConsentResponse(fromEu && givenConsent == unknown, givenConsent == nonPersonalized))
    }
}