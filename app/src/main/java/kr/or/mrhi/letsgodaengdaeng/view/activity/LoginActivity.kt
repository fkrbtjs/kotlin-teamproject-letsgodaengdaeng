package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityLoginBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO

class LoginActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    val REQ_READ = 52
    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** 퍼미션 승인 여부 점검 */
        if (!isPermitted()) {
            ActivityCompat.requestPermissions(this, permissions, REQ_READ)
        }

        /** 로그인 */
        binding.btnLogin.setOnClickListener {
            var loginFlag = false
            val userDAO = UserDAO()
            userDAO.selectUserType("phone","${binding.edtPhone.text}")?.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e(TAG, "${snapshot.value}")
                    for (userData in snapshot.children) {
                        val user = userData.getValue(User::class.java)
                        if (user?.password == "${binding.edtPassword.text}") {
                            Toast.makeText(this@LoginActivity, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            loginFlag = true
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("userCode", userData.key)
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

    /** 권한 승인 여부 */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_READ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한을 승인을 완료하였습니다.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "권한을 승인해야 어플 사용이 가능합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /** 외부 저장소 읽을 권한 체크 */
    fun isPermitted(): Boolean {
        return ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED
    }
}