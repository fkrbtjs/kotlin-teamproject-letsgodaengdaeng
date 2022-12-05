package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(
    var num: String? = null,
    var name: String? = null,
    var breeds: String? = null,
    var gender: String? = null,
    var age: String? = null,
    var weight: String? = null,
    var intro: String? = null
)  : Parcelable {

    companion object : Parceler<Animal> {
        override fun create(parcel: Parcel): Animal {
            return Animal(parcel)
        }

        override fun Animal.write(parcel: Parcel, flags: Int) {
            parcel.writeString(num)
            parcel.writeString(name)
            parcel.writeString(breeds)
            parcel.writeString(gender)
            parcel.writeString(age)
            parcel.writeString(weight)
            parcel.writeString(intro)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
}
