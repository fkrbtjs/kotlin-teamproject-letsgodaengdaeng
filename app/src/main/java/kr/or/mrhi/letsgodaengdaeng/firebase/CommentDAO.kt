package kr.or.mrhi.letsgodaengdaeng.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO

class CommentDAO {
    var databaseReference: DatabaseReference? = null
    //firebase Storage
    var storage: FirebaseStorage? = null

    init {
        //firebase  데이타베이스 객체화
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("comment")
        storage = Firebase.storage
    }

    //realtime database user 내용 입력하기
    fun insertCommunity(community: CommunityVO?): Task<Void> {
        //insert into user(userKey),userName,userAge, userPhone) values('keyValue','nameValue', ~~~)
        return databaseReference!!.push().setValue(community)
    }

    fun selectComment(communityID:String): Query? {
        return databaseReference?.orderByChild("communityID")?.equalTo(communityID)
    }

    fun selectCommunity2(docID: String): Query? {
        return databaseReference!!.child(docID)
    }

    fun selectFriendCommunity(): Query?{
        return databaseReference?.orderByChild("category")?.equalTo("친구해요")
    }
    fun selectShareCommunity(): Query?{
        return databaseReference?.orderByChild("category")?.equalTo("공유해요")
    }
    fun selectQuestionCommunity(): Query?{
        return databaseReference?.orderByChild("category")?.equalTo("궁금해요")
    }

    //realtime database user update
    fun updateCommunity(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    // realtime database user delete
    fun deleteComment(key:String) : Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}