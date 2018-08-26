package nl.entreco.data.stream

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
internal data class IceServerFirebaseApiData(
    @PropertyName("uri")
    val uri: String? = null,

    @PropertyName("username")
    val username: String? = null,

    @PropertyName("password")
    val password: String? = null
)