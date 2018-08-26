package nl.entreco.data.stream

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@Keep
@IgnoreExtraProperties
internal data class IceCandidateFirebaseApiData(

    @SerializedName("sdpMid")
    var sdpMid: String = "",

    @SerializedName("sdpMLineIndex")
    var sdpMLineIndex: Int = -1,

    @SerializedName("sdp")
    var sdp: String = ""
)