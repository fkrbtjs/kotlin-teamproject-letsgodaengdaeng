package kr.or.mrhi.letsgodaengdaeng.dataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class CommunityVO(
    var docID: String? = "",
    var userID : String? = "",
    var nickname: String? = "",
    var local: String? = "",
    var date: String? = "",
    var category: String? = "",
    var content: String? = "",
    var likeCount : Int = 0,
    var commentCount : Int = 0
): Parcelable {

    companion object : Parceler<CommunityVO> {
        override fun create(parcel: Parcel): CommunityVO {
            return CommunityVO(parcel)
        }

        override fun CommunityVO.write(parcel: Parcel, flags: Int) {
            parcel.writeString(docID)
            parcel.writeString(userID)
            parcel.writeString(nickname)
            parcel.writeString(local)
            parcel.writeString(date)
            parcel.writeString(category)
            parcel.writeString(content)
            parcel.writeInt(likeCount)
            parcel.writeInt(commentCount)
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
        parcel.readInt(),
        parcel.readInt()
    )

}