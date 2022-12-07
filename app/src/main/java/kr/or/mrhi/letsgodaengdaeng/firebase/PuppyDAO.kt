package kr.or.mrhi.letsgodaengdaeng.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.dataClass.User

class PuppyDAO {
    var databaseReference: DatabaseReference? = null
    var storage: FirebaseStorage? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("puppy")
        storage = Firebase.storage
    }

    fun signUpPuppy(userCode: String, puppy: Puppy): Task<Void> {
        return databaseReference!!.child(userCode).setValue(puppy)
    }

    fun selectPuppy(userCode: String): Query? {
        return databaseReference!!.child(userCode)
    }

    fun updatePuppy(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

}
