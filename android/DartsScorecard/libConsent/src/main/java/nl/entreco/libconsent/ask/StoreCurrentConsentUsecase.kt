package nl.entreco.libconsent.ask

import nl.entreco.libconsent.ConsentPrefs
import javax.inject.Inject

class StoreCurrentConsentUsecase @Inject constructor(
        private val prefs: ConsentPrefs
) {
    fun go(status: String?, eu: Boolean? = null) {
        prefs.store(status, eu)
    }

    fun clear() {
        prefs.clear()
    }
}