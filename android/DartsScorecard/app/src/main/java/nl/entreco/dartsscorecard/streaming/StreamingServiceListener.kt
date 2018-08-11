package nl.entreco.dartsscorecard.streaming

import org.webrtc.PeerConnection

interface StreamingServiceListener {

    /**
     * When receiving this exception service is in unrecoverable state and will call stopSelf, bound view(if any) should unbind
     */
    fun criticalWebRTCServiceException(throwable: Throwable)

    fun connectionStateChange(iceConnectionState: PeerConnection.IceConnectionState)
}