package nl.entreco.libconsent.fetch

data class FetchConsentResponse(val shouldAskForConsent: Boolean, val nonPersonalized: Boolean)