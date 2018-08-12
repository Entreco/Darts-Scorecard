package nl.entreco.dartsscorecard.streaming

import nl.entreco.shared.log.Logger
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

internal class WebRtcOfferingPartyHandler (
        private val logger: Logger,
        private val peer: PeerConnection,
        private val webRtcActionListener: Listener) {

    interface Listener {
        /**
         * Triggered in case of internal errors.
         */
        fun onError(error: String)

        /**
         * Called when local session description from offering party is created.
         * [localSessionDescription] object should be sent to the other party through established connection channel.
         */
        fun onOfferRemoteDescription(localSessionDescription: SessionDescription)
    }

    companion object {
        private val TAG = WebRtcOfferingPartyHandler::class.java.simpleName
    }

    fun createOffer(offerAnswerConstraints: MediaConstraints) {
        logger.d(TAG, "Creating offer with $offerAnswerConstraints")
        peer.createOffer(object : SdpObserver {
            override fun onCreateSuccess(localSessionDescription: SessionDescription) {
                setLocalOfferDescription(localSessionDescription)
            }

            override fun onCreateFailure(error: String) {
                webRtcActionListener.onError(error)
            }

            override fun onSetFailure(p0: String?) {}

            override fun onSetSuccess() {}
        }, offerAnswerConstraints)
    }

    private fun setLocalOfferDescription(localSessionDescription: SessionDescription) {
        peer.setLocalDescription(object : SdpObserver {

            override fun onSetSuccess() {
                webRtcActionListener.onOfferRemoteDescription(localSessionDescription)
            }

            override fun onSetFailure(error: String) {
                webRtcActionListener.onError(error)
            }

            override fun onCreateSuccess(p0: SessionDescription?) {}

            override fun onCreateFailure(p0: String?) {}
        }, localSessionDescription)
    }

    fun handleRemoteAnswer(remoteSessionDescription: SessionDescription) {
        peer.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {
                logger.d(TAG, "Remote description from answer set successfully")
            }

            override fun onSetFailure(error: String) {
                webRtcActionListener.onError(error)
            }

            override fun onCreateSuccess(p0: SessionDescription?) {
                logger.w("onCreateSuccess")
            }

            override fun onCreateFailure(p0: String?) {
                logger.w("onCreateFailure")
            }
        }, remoteSessionDescription)
    }
}