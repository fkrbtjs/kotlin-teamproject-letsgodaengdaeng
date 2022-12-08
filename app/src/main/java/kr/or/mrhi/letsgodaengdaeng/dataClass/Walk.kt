package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class Walk(
    var data: String? = "",
    var time: String? = "",
    var point: String? = "",
    var userID : String? = ""
) :
    Parcelable {

    companion object : Parceler<Walk> {
        override fun create(parcel: Parcel): Walk {
            return Walk(parcel)
        }

        override fun Walk.write(parcel: Parcel, flags: Int) {
            parcel.writeString(data)
            parcel.writeString(time)
            parcel.writeString(point)
            parcel.writeString(userID)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
}