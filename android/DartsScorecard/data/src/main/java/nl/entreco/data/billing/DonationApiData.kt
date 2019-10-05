package nl.entreco.data.billing

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

/**
 * Created by entreco on 19/02/2018.
 */
@Keep
@IgnoreExtraProperties
internal data class DonationApiData(
        @SerializedName("sku")
        var productId: String,

        @SerializedName("price")
        var price: String,

        @SerializedName("title")
        var title: String,

        @SerializedName("description")
        var description: String,

        @SerializedName("price_currency_code")
        var priceCurrencyCode: String,

        @SerializedName("price_amount_micros")
        var priceAmountMicros: String
)
