package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class WalkMarker(
    var latitude: String?,
    var longitude: String?,
) :
    Parcelable {

    companion object : Parceler<WalkMarker> {
        override fun create(parcel: Parcel): WalkMarker {
            return WalkMarker(parcel)
        }

        override fun WalkMarker.write(parcel: Parcel, flags: Int) {
            parcel.writeString(latitude)
            parcel.writeString(longitude)

        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
    )
}