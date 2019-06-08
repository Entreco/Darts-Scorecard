package nl.entreco.libconsent.ask

sealed class AskConsentResponse() {
    object Normal : AskConsentResponse()
    object NonPersonalized : AskConsentResponse()
    object PreferPaid : AskConsentResponse()

    companion object {
        fun normal(): AskConsentResponse = Normal
        fun npa(): AskConsentResponse = NonPersonalized
        fun paid(): AskConsentResponse = PreferPaid
    }
}