package nl.entreco.data.stream

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
internal data class ReceiverFirebaseApiData(

        @PropertyName("code")
        val code: String = "",

        @PropertyName("inCall")
        val inCall: Boolean = false
)