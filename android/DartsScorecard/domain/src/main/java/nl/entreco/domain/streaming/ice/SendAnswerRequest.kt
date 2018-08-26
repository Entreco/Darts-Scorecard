package nl.entreco.domain.streaming.ice

data class SendAnswerRequest(val recipientUuid: String, val localSessionDescription: DscSessionDescription)