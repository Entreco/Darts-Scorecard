package nl.entreco.dartsscorecard.beta.donate

import nl.entreco.domain.beta.Donation

sealed class DonationEvent{
    object None : DonationEvent()
    data class Purchase(val donation: Donation) : DonationEvent()
}