package kr.or.mrhi.letsgodaengdaeng.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO

class CommunityDAO {
    var databaseReference: DatabaseReference? = null

    //firebase Storage
    var storage: FirebaseStorage? = null

    init {
        //firebase  데이타베이스 객체화
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("community")
        storage = Firebase.storage
    }

    //realtime database user 내용 입력하기
    fun insertCommunity(community: CommunityVO?): Task<Void> {
        //insert into user(userKey),userName,userAge, userPhone) values('keyValue','nameValue', ~~~)
        return databaseReference!!.push().setValue(community)
    }

    fun selectCommunity(): Query? {
        return databaseReference
    }

    fun selectCommunity2(docID: String): Query? {
        return databaseReference!!.child(docID)
    }

    fun updateCommentCount(docID:String,hashMap: HashMap<String, Any>):Task<Void>{
        return databaseReference!!.child("${docID}").updateChildren(hashMap)
    }

    fun selectComment(communityID:String):Query?{
        return databaseReference?.child(communityID)?.child("comment")?.orderByChild("communityID")?.equalTo(communityID)
    }

    fun deleteComment(communityID:String,commentID:String){
        databaseReference?.child(communityID)?.child("comment")?.child(commentID)?.removeValue()
    }

    fun plusLikeCount(docID: String, userCode: String){
        val likeHashMap: HashMap<String, Any> = HashMap()
        likeHashMap[userCode] = true
        databaseReference!!.child(docID).child("like").updateChildren(likeHashMap)

    }

    fun minusLikeCount(docID:String, userCode: String) {
        Log.d("key3", "${databaseReference!!.child(docID).child("like").child(userCode)}")
        databaseReference!!.child(docID).child("like").child(userCode).removeValue()

    }

    fun selectFriendCommunity(): Query? {
        return databaseReference?.orderByChild("category")?.equalTo("친구해요")
    }

    fun selectShareCommunity(): Query? {
        return databaseReference?.orderByChild("category")?.equalTo("공유해요")
    }

    fun selectQuestionCommunity(): Query? {
        return databaseReference?.orderByChild("category")?.equalTo("궁금해요")
    }

    //realtime database user update
    fun updateCommunity(docID: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(docID).updateChildren(hashMap)
    }

    // realtime database user delete
    fun deleteCommunity(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}