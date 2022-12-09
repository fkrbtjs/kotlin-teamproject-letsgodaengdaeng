package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class WalkMarker(
    var userID: String? = null,
    var latitude: String? = null,
    var longitude: String? = null
) :
    Parcelable {

    companion object : Parceler<WalkMarker> {
        override fun create(parcel: Parcel): WalkMarker {
            return WalkMarker(parcel)
        }

        override fun WalkMarker.write(parcel: Parcel, flags: Int) {
            parcel.writeString(userID)
            parcel.writeString(latitude)
            parcel.writeString(longitude)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
}