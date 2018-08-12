package nl.entreco.domain.streaming.ice

data class CreateOfferRequest(val recipientUuid: String, val description: DscSessionDescription)