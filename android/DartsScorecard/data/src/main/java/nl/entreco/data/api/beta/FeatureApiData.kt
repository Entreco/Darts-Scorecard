package nl.entreco.data.api.beta

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

/**
 * Created by entreco on 03/02/2018.
 */
@Keep
@IgnoreExtraProperties
internal data class FeatureApiData(

        @PropertyName("description")
        val description: String,

        @PropertyName("image")
        val image: String,

        @PropertyName("title")
        val title: String,

        @PropertyName("remco")
        val remco: String?,

        @PropertyName("goal")
        val goal: Int,

        @PropertyName("count")
        val count: Int
)
