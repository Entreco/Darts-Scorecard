package nl.entreco.data.stream

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
internal class SessionDescriptionFirebaseApiData {

    @PropertyName("uuid")
    var uuid: String = ""

    @PropertyName("type")
    var type: Int = 0

    @PropertyName("description")
    var description: String? = null
}