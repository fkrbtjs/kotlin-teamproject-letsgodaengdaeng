package kr.or.mrhi.letsgodaengdaeng.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.dataClass.Walk

class WalkDAO {
    var databaseReference: DatabaseReference? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("walk")
    }

    fun insert(walk: Walk): Task<Void> {
        return databaseReference!!.push().setValue(walk)
    }

    fun select(userCode: String): Query? {
        return databaseReference!!.orderByChild("userID").equalTo(userCode)
    }


}