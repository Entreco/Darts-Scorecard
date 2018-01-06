package nl.entreco.domain.setup.game

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Entreco on 12/12/2017.
 */
data class CreateGameRequest(val startScore: Int, val startIndex: Int, val numLegs: Int, val numSets: Int) : Parcelable {
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

    companion object CREATOR : Parcelable.Creator<CreateGameRequest> {
        override fun createFromParcel(parcel: Parcel): CreateGameRequest {
            return CreateGameRequest(parcel)
        }

        override fun newArray(size: Int): Array<CreateGameRequest?> {
            return arrayOfNulls(size)
        }
    }
}