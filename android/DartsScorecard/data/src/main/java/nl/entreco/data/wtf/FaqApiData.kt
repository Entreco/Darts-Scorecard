package nl.entreco.data.wtf

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
internal class FaqApiData {
    @PropertyName("desc")
    val desc: String = ""

    @PropertyName("image")
    val image: String = ""

    @PropertyName("title")
    val title: String = ""

    @PropertyName("video")
    val video: String = ""

    @PropertyName("viewed")
    val viewed: Long = 0
}
