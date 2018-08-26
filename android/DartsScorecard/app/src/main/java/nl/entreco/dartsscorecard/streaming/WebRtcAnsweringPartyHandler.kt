package nl.entreco.dartsscorecard.streaming

import nl.entreco.shared.log.Logger
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

internal class WebRtcAnsweringPartyHandler(
        private val logger: Logger,
        private val peer: PeerConnection,
        private val offerAnswerConstraints: MediaConstraints,
        private val listener: Listener) {

    interface Listener {
        /**
         * Triggered in case of internal errors.
         */
        fun onError(error: String)

        /**
         * Triggered when local session description from answering party is created.
         * [localSessionDescription] object should be sent to the other party through established connection channel.
         */
        fun onSuccess(localSessionDescription: SessionDescription)
    }

    fun handleRemoteOffer(remoteSessionDescription: SessionDescription) {
        peer.setRemoteDescription(object : SdpObserver{
            override fun onSetFailure(error: String) {
                logger.w("onSetFailure")
                listener.onError(error)
            }

            override fun onSetSuccess() {
                logger.w("onSetSuccess")
                createAnswer()
            }

            override fun onCreateSuccess(p0: SessionDescription?) {
                logger.w("onCreateSuccess")
            }

            override fun onCreateFailure(p0: String?) {
                logger.w("onCreateFailure")
            }
        }, remoteSessionDescription)
    }

    private fun createAnswer() {

        peer.createAnswer(object : SdpObserver {
            override fun onSetFailure(p0: String?) {
                logger.w("onSetFailure")
            }

            override fun onSetSuccess() {
                logger.w("onSetSuccess")
            }

            override fun onCreateSuccess(sessionDescription: SessionDescription) {
                logger.w("onCreateSuccess")
                setLocalAnswerDescription(sessionDescription)
            }

            override fun onCreateFailure(error: String) {
                logger.w("onCreateFailure")
                listener.onError(error)
            }

        }, offerAnswerConstraints)
    }

    private fun setLocalAnswerDescription(sessionDescription: SessionDescription) {

        peer.setLocalDescription(object : SdpObserver {

            override fun onCreateSuccess(p0: SessionDescription?) {
                logger.w("onCreateSuccess")
            }

            override fun onCreateFailure(p0: String?) {
                logger.w("onCreateFailure")
            }

            override fun onSetSuccess() {
                logger.w("onSetSuccess")
                listener.onSuccess(sessionDescription)
            }

            override fun onSetFailure(error: String) {
                logger.w("onSetFailure")
                listener.onError(error)
            }
        }, sessionDescription)
    }
}