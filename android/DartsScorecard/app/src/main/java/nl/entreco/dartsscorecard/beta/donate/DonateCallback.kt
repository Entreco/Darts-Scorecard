package nl.entreco.dartsscorecard.beta.donate

import androidx.lifecycle.Lifecycle
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakePurchaseResponse

/**
 * Created by entreco on 09/02/2018.
 */
interface DonateCallback {
    fun lifeCycle(): Lifecycle
    fun makeDonation(response: MakePurchaseResponse)
    fun onDonationMade(donation: Donation)
}