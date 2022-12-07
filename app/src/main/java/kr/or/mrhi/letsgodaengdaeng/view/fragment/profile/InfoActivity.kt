package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityInfoBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.PuppyDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class InfoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userID = intent.getStringExtra("userID")

        if (userID.equals(MainActivity.userCode)){
            binding.ivUpdate.visibility = View.VISIBLE
        }

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ivUpdate.setOnClickListener{
            val intent = Intent(this, UpdateActivity::class.java)
            startActivity(intent)
        }
        /** firebase storage 의 현재 로그인된 유저가 저장한 이미지를 불러온다*/
        val puppyDAO = PuppyDAO()
        val puppyImg = puppyDAO.storage!!.reference.child("puppyImage/${userID}.jpg")
        puppyImg.downloadUrl.addOnCompleteListener{
            if(it.isSuccessful){
                Glide.with(applicationContext)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }

        val userDAO = UserDAO()
        val userImg = userDAO.storage!!.reference.child("userImage/${userID}.jpg")
        userImg.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful){
                Glide.with(applicationContext)
                    .load(it.result)
                    .into(binding.ivUserPicture)
            }
        }

        /** firebase 반려견 정보를 불러온다*/
        puppyDAO.selectPuppy(userID!!)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val puppy: Puppy? = snapshot.getValue(Puppy::class.java)
                    binding.tvPuppyName.text = puppy?.name
                    binding.tvPuppyGender.text = puppy?.gender
                    binding.tvPuppyBreed.text = puppy?.breed
                    binding.tvPuppySize.text = puppy?.size
                    binding.tvPuppyTendency.text = puppy?.tendency
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        /** firebase 유저 정보를 불러온다*/
        userDAO.selectUser(userID)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val user: User? = snapshot.getValue(User::class.java)
                    binding.tvUserName.text = user?.nickname
                    binding.tvUserPhone.text = user?.phone
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //뒤로가기버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}