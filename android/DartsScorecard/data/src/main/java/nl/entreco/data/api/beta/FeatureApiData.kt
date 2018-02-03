package nl.entreco.data.api.beta

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

/**
 * Created by entreco on 03/02/2018.
 */
@IgnoreExtraProperties
class FeatureApiData {

    @PropertyName("description")
    val description: String = ""

    @PropertyName("img")
    val img: String = ""

    @PropertyName("title")
    val title: String = ""

    @PropertyName("required")
    val required: Int = 0

    @PropertyName("votes")
    val votes: Int = 0
}