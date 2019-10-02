package nl.entreco.domain.beta.donations

import nl.entreco.domain.beta.Donation

/**
 * Created by entreco on 09/02/2018.
 */
sealed class FetchDonationsResponse{
    data class Ok(val donations: List<Donation>, val needToBeConsumed: Boolean):FetchDonationsResponse()
    data class Error(val error: Throwable, val hasPreviouslyBoughtItems: Boolean):FetchDonationsResponse()
}