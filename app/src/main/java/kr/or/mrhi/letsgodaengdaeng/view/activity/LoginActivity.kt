package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityLoginBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO

class LoginActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var use: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** 로그인 */
        binding.btnLogin.setOnClickListener {
            var loginFlag = false
            val userDAO = UserDAO()
            userDAO.selectUserPhone("${binding.edtPhone.text}")?.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e(TAG, "${snapshot.value}")
                    for (userData in snapshot.children) {
                        val user = userData.getValue(User::class.java)
                        if (user?.password == "${binding.edtPassword.text}") {
                            Toast.makeText(this@LoginActivity, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            loginFlag = true
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("uid", userData.key)
                            startActivity(intent)
                            finish()
                        }
                    }
                    if (!loginFlag) {
                        Toast.makeText(this@LoginActivity, "핸드폰과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e (TAG, "selectUserPhone onCancelled $error")
                }
            })
        }

        /** 회원가입 */
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupUserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


