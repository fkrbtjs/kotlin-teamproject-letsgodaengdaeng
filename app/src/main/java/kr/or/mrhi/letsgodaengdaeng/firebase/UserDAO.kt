package kr.or.mrhi.letsgodaengdaeng.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.letsgodaengdaeng.dataClass.User

class UserDAO {
    var databaseReference: DatabaseReference? = null
    var storage: FirebaseStorage? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("user")
        storage = Firebase.storage
    }

    /**********************************************************************************************/

    fun signUpUser(userCode: String, user: User?): Task<Void> {
        return databaseReference!!.child(userCode).setValue(user)
    }

    fun selectUser(): Query? {
        return databaseReference
    }

    fun updateUser(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    fun deleteUser(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}