package nl.entreco.domain.beta.donations

import nl.entreco.domain.beta.Donation

/**
 * Created by entreco on 09/02/2018.
 */
class FetchDonationsResponse(val donations: List<Donation>, val needToBeConsumed: Boolean)