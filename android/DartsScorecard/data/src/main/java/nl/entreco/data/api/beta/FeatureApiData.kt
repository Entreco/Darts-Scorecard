package nl.entreco.data.api.beta

import com.google.gson.annotations.SerializedName

/**
 * Created by entreco on 03/02/2018.
 */
class FeatureApiData {

//    {
//        "description" : "Allow to cast games on your TV",
//        "img" : "https://codelabs.developers.google.com/codelabs/cast-videos-android/img/b799bd87a0e579aa.png",
//        "required" : 10000,
//        "title" : "Cast to TV",
//        "votes" : {
//        "remco" : 5
//    }

    @SerializedName("description")
    val description: String = ""

    @SerializedName("img")
    val image: String = ""

    @SerializedName("title")
    val title: String = ""

    @SerializedName("required")
    val required: Int = 0
}