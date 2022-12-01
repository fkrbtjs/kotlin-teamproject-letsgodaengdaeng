package kr.or.mrhi.letsgodaengdaeng.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.ktx.storageMetadata
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentProfileBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.InfoActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.SettingActivity

class ProfileFragment : Fragment() {
    var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        val puppy = Puppy()

        selectUser()

        //firebase storage 에서 사진 가져오기.
        val userDAO = UserDAO()
        //firebase storage 이미지 경로를 알려준다
        val img = userDAO.storage!!.reference.child("images/.png")

        img.downloadUrl.addOnCompleteListener{
            if(it.isSuccessful){
                Glide.with(container!!.context)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }

        binding.ivSetting.setOnClickListener{
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.cdIdCard.setOnClickListener{
            val intent = Intent(context, InfoActivity::class.java)
            startActivity(intent)
        }

        binding.tvName.text = puppy.name
        binding.tvGender.text = puppy.gender
        binding.tvBreed.text = puppy.breed

        return binding.root
    }

    private fun selectUser() {
        val userDAO = UserDAO()
        userDAO.selectUser()?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val user = userData.getValue(User::class.java)
//                    user?.phone = userData.key.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}