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
import kr.or.mrhi.letsgodaengdaeng.dataClass.WalkMarker

class WalkDAO {
    var databaseReference: DatabaseReference? = null
    var databaseReferenceMarker: DatabaseReference? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("walk")
        databaseReferenceMarker = db.getReference("marker")
    }

    fun insert(walk: Walk): Task<Void> {
        return databaseReference!!.push().setValue(walk)
    }

    fun select(userCode: String): Query? {
        return databaseReference!!.orderByChild("userID").equalTo(userCode)
    }

    fun insertMarker(walkMarker: WalkMarker): Task<Void> {
        return databaseReferenceMarker!!.push().setValue(walkMarker)
    }

    fun selectMarker(): Query? {
        return databaseReferenceMarker
    }
}