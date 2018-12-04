package nl.entreco.data.api.beta

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

/**
 * Created by entreco on 03/02/2018.
 */
@Keep
@IgnoreExtraProperties
internal class FeatureApiData {

    @PropertyName("description")
    val description: String = ""

    @PropertyName("image")
    val image: String = ""

    @PropertyName("title")
    val title: String = ""

    @PropertyName("remarks")
    val remarks: String? = ""

    @PropertyName("goal")
    val goal: Int = 0

    @PropertyName("count")
    val count: Int = 0

    @PropertyName("video")
    val video: String = ""
}
