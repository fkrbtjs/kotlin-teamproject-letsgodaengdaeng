package kr.or.mrhi.letsgodaengdaeng.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class CommunityDAO {
    var databaseReference: DatabaseReference? = null
    var storage: FirebaseStorage? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("community")
        storage = Firebase.storage
    }

    fun selectCommunity(): Query? {
        return databaseReference
    }

    fun selectCommunityID(docID: String): Query? {
        return databaseReference!!.child(docID)
    }

    fun selectMyComment(communityID:String, userID:String) : Query?{
        return databaseReference?.child(communityID)?.child("comment")?.orderByChild("userID")?.equalTo(userID)
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

    fun selectFriendCommunity(): Query? {
        return databaseReference?.orderByChild("category")?.equalTo("친구해요")
    }

    fun selectShareCommunity(): Query? {
        return databaseReference?.orderByChild("category")?.equalTo("공유해요")
    }

    fun selectQuestionCommunity(): Query? {
        return databaseReference?.orderByChild("category")?.equalTo("궁금해요")
    }

    fun updateCommunity(docID: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(docID).updateChildren(hashMap)
    }

    fun deleteCommunity(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}