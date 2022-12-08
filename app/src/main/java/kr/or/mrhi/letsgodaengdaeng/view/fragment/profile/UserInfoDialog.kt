package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityUserdialogBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class UserInfoDialog(val context: Context) {
    val dialog = Dialog(context)

    fun showDialog(){
        val binding = ActivityUserdialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val userDAO = UserDAO()

        /** firebase storage 의 현재 로그인된 유저가 저장한 이미지를 불러온다*/
        val userImg = userDAO.storage!!.reference.child("userImage/${MainActivity.userCode}.jpg")
        userImg.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful){
                Glide.with(this.context)
                    .load(it.result)
                    .into(binding.ivUserImage)
            }
        }

        /** firebase 유저 정보를 불러온다*/
        userDAO.selectUser(MainActivity.userCode!!)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val user: User? = snapshot.getValue(User::class.java)
                    binding.tvUserNic.text = user?.nickname
                    binding.tvUserPhone.text = user?.phone
                    binding.tvAddress.text = user?.address
                    binding.tvIntroduce.text = user?.introduce
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }
}