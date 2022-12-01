package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.set
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityWithdrawalBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.LoginActivity

class WithdrawalActivity : AppCompatActivity() {
    lateinit var binding: ActivityWithdrawalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cbConsent.setOnClickListener {
            if(binding.cbConsent.isChecked) {
                binding.btnWithdrawal.visibility = View.VISIBLE
                Log.d("profileactivity", "나와라")
            }else{
                binding.btnWithdrawal.visibility = View.INVISIBLE
                Log.d("profileactivity", "숨어라")
            }
        }

        binding.btnWithdrawal.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val dialog: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            val userDAO = UserDAO()
            dialog.setTitle("회원탈퇴")
            dialog.setMessage("패스워드를 입력해주세요")
            dialog.setView(R.layout.dialog_layout)
            dialog.setPositiveButton("예", DialogInterface.OnClickListener{ dialog, which ->
                userDAO.selectUser()?.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val text = findViewById<EditText>(R.id.edt_dialog)
                        for(userData in snapshot.children){
                            val user = userData.getValue(User::class.java) // json 파일을 클래스파일로 만든다
                            Log.d("letsgodaengdaeng", "${user.toString()}")
                            if (user?.password.toString() == "${text.setText("123asd")}"){
                                Toast.makeText(this@WithdrawalActivity, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                                Log.e("letsgodaengdaeng", "로그아웃 ${user?.password.toString() == "123asd"}")
                            }else{
                                Log.e("letsgodaengdaeng", "로그아웃실패")
                                dialog.dismiss()
                            }
                        }// end of for
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("letsgodaengdaeng", "${error.toString()}")
                    }
                })



//                deleteData()
//                startActivity(intent)

                finish()
            })
            dialog.setNegativeButton("아니오", DialogInterface.OnClickListener{ dialog, which ->
                dialog.dismiss()
            })
            dialog.show()
        }
    }

    private fun deleteData() {
        val userDAO = UserDAO()
        userDAO.deleteUser("2uWa4YHCNfPg0qMFjRtjvMFHtfg2").addOnSuccessListener {
            Toast.makeText(applicationContext, "User delete success", Toast.LENGTH_SHORT).show()
            Log.d("firebasecrud", "User delete success")
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "User delete no", Toast.LENGTH_SHORT).show()
            Log.d("firebasecrud", "User delete no")
        }
    }

}