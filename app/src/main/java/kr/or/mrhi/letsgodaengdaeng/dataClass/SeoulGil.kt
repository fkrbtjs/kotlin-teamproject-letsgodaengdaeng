package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeoulGil(
    var name: String? = null,
    var local: String? = null,
    var distance: String? = null,
    var time: String? = null,
    var detailCourse: String? = null,
    var courseLevel: String? = null,
    var content: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null
) : Parcelable {

    companion object : Parceler<SeoulGil> {
        override fun create(parcel: Parcel): SeoulGil {
            return SeoulGil(parcel)
        }

        override fun SeoulGil.write(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(local)
            parcel.writeString(distance)
            parcel.writeString(time)
            parcel.writeString(detailCourse)
            parcel.writeString(courseLevel)
            parcel.writeString(content)
            parcel.writeDouble(longitude!!)
            parcel.writeDouble(latitude!!)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    )
}
