package nl.entreco.libconsent.retrieve

sealed class RetrieveConsentResponse {
    data class Success(val status: String) : RetrieveConsentResponse()
    data class Error(val description: String) : RetrieveConsentResponse()

    companion object {
        fun success(status: String): RetrieveConsentResponse = Success(status)
        fun failure(description: String): RetrieveConsentResponse = Error(description)
    }
}