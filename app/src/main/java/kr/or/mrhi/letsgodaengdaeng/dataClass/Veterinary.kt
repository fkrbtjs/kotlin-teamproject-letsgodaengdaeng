package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Veterinary (var code: String?, var name: String?, var address: String?, var phone: String?) :
    Parcelable {

    companion object : Parceler<Veterinary> {
        override fun create(parcel: Parcel): Veterinary {
            return Veterinary(parcel)
        }

        override fun Veterinary.write(parcel: Parcel, flags: Int) {
            parcel.writeString(code)
            parcel.writeString(name)
            parcel.writeString(address)
            parcel.writeString(phone)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    )

}
