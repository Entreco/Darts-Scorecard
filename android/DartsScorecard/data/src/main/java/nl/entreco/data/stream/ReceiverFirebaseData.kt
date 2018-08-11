package nl.entreco.data.stream

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

internal data class ReceiverFirebaseData(val code: String, val inCall: Boolean = false)

@Keep
@IgnoreExtraProperties
internal class ReceiverFirebaseApiData {

    @PropertyName("code")
    val code: String = ""

    @PropertyName("inCall")
    val inCall: Boolean = false
}