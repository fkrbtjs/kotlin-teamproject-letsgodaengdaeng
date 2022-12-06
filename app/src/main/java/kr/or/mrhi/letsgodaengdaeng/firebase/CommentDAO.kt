package kr.or.mrhi.letsgodaengdaeng.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class CommentDAO {
    var databaseReference: DatabaseReference? = null

    var storage: FirebaseStorage? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("comment")
        storage = Firebase.storage
    }
}