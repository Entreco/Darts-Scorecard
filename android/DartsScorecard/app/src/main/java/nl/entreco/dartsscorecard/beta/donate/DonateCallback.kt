package nl.entreco.dartsscorecard.beta.donate

import android.arch.lifecycle.Lifecycle
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse

/**
 * Created by entreco on 09/02/2018.
 */
interface DonateCallback {
    fun lifeCycle(): Lifecycle
    fun makeDonation(response: MakeDonationResponse)
    fun onDonationMade(donation: Donation)
}