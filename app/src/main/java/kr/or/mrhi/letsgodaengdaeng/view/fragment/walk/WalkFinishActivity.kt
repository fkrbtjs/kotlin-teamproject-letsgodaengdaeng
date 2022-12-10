package kr.or.mrhi.letsgodaengdaeng.view.fragment.walk

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.dataClass.Walk
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityWalkFinishBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.PuppyDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.WalkDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class WalkFinishActivity : AppCompatActivity() {
    lateinit var walk: Walk
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWalkFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = SimpleDateFormat("yy-MM-dd").format(Date())
        val time = intent.getStringExtra("time")
        val point = intent.getIntExtra("point",0)

        binding.tvDate.text = data
        binding.tvTime.text = time
        binding.tvPoint.text = point.toString()

        val userDAO = UserDAO()
        userDAO.selectUser(MainActivity.userCode!!)?.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userPoint = snapshot.getValue(User::class.java)?.point
                val plusPoint = userPoint?.toInt()?.plus(point)

                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["point"] = plusPoint.toString()

                userDAO.updateUser(MainActivity.userCode!!, hashMap).addOnSuccessListener {
                    Log.d("WalkFinishActivity", "user update success")
                }.addOnFailureListener {
                    Log.d("WalkFinishActivity", "user update fail")
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("WalkFinishActivity", "${error.details}")
            }

        })

        val userImg = userDAO.storage!!.reference.child("userImage/${MainActivity.userCode}.jpg")
        userImg.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(this)
                    .load(it.result)
                    .circleCrop()
                    .into(binding.ivUser)
            }
        }

        val puppyDAO = PuppyDAO()
        val puppyImg = puppyDAO.storage!!.reference.child("puppyImage/${MainActivity.userCode}.jpg")
        puppyImg.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(this)
                    .load(it.result)
                    .circleCrop()
                    .into(binding.ivPuppy)
            }
        }

        walk = Walk(data, time, point.toString(), MainActivity.userCode)
        val walkDAO = WalkDAO()
        walkDAO.insert(walk)
    }

    /** 툴바 백버튼 누르면 메인으
     * 로 돌아감 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}