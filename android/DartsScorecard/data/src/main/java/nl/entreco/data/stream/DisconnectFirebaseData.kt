package nl.entreco.data.stream

import android.support.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@Keep
@IgnoreExtraProperties
internal data class DisconnectFirebaseData(@SerializedName("from") val from: String)