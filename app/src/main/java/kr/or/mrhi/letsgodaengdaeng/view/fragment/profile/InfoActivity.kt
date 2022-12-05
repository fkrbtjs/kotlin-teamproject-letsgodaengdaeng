package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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

class InfoActivity(val docID:String) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ivUpdate.setOnClickListener{
            val intent = Intent(this, UpdateActivity::class.java)
            startActivity(intent)
        }

        val puppyDAO = PuppyDAO()
        puppyDAO.selectPuppy(docID)?.addValueEventListener(object :
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

        val userDAO = UserDAO()
        val puppyImg = puppyDAO.storage!!.reference.child("puppyImage/${MainActivity.userCode}.jpg")
        puppyImg.downloadUrl.addOnCompleteListener{
            if(it.isSuccessful){
                Glide.with(applicationContext)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }
        userDAO.selectUser(docID)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val user: User? = snapshot.getValue(User::class.java)
                    binding.tvUserName.text = user?.nickname
                    binding.tvUserPhone.text = user?.phone
                    binding.tvUserIntroduce.text = user?.introduce
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