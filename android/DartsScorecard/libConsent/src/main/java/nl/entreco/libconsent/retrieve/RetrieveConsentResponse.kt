package nl.entreco.libconsent.retrieve

sealed class RetrieveConsentResponse {
    data class Success(val status: String, val eu: Boolean) : RetrieveConsentResponse()
    data class Error(val description: String) : RetrieveConsentResponse()

    companion object {
        fun success(status: String, eu: Boolean): RetrieveConsentResponse = Success(status, eu)
        fun failure(description: String): RetrieveConsentResponse = Error(description)
    }
}