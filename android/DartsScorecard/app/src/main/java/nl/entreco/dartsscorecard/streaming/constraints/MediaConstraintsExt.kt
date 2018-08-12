package nl.entreco.dartsscorecard.streaming.constraints

import org.webrtc.MediaConstraints

internal fun MediaConstraints.addConstraints(constraints: WebRtcConstraints<*, *>) {
    mandatory.addAll(constraints.mandatoryKeyValuePairs)
    optional.addAll(constraints.optionalKeyValuePairs)
}

internal fun MediaConstraints.addConstraints(vararg constraints: WebRtcConstraints<*, *>) {
    constraints.forEach {
        addConstraints(it)
    }
}