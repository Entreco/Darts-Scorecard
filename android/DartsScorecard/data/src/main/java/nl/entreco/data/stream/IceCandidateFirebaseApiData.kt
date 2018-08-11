package nl.entreco.data.stream

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@Keep
@IgnoreExtraProperties
internal class IceCandidateFirebaseApiData {

    @SerializedName("sdpMid")
    val sdpMid: String = ""

    @SerializedName("sdpMLineIndex")
    val sdpMLineIndex: Int = -1

    @SerializedName("sdp")
    val sdp: String = ""
}