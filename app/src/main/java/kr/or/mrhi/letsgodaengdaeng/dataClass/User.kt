package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize


@Parcelize         //User Info
data class User(val phone: String? = null, val password: String? = null,
                val nickname: String? = null, val introduce: String? =null, val address: String? =null) : Parcelable {

    companion object : Parceler<User> {
        override fun create(parcel: Parcel): User {
            return User(parcel)
        }

        override fun User.write(parcel: Parcel, flags: Int) {
            parcel.writeString(phone)
            parcel.writeString(password)
            parcel.writeString(nickname)
            parcel.writeString(introduce)
            parcel.writeString(address)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

}
