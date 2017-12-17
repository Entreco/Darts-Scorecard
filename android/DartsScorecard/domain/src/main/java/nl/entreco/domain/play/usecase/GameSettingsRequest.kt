package nl.entreco.domain.play.usecase

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Entreco on 12/12/2017.
 */
data class GameSettingsRequest(val startScore: Int, val startIndex: Int, val numLegs: Int, val numSets: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(startScore)
        parcel.writeInt(startIndex)
        parcel.writeInt(numLegs)
        parcel.writeInt(numSets)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameSettingsRequest> {
        override fun createFromParcel(parcel: Parcel): GameSettingsRequest {
            return GameSettingsRequest(parcel)
        }

        override fun newArray(size: Int): Array<GameSettingsRequest?> {
            return arrayOfNulls(size)
        }
    }
}