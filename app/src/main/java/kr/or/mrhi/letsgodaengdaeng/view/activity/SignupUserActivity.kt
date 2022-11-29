package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySignupUserBinding
import java.util.concurrent.TimeUnit

class SignupUserActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupUserBinding
    val auth = Firebase.auth
    var verificationId: String? = null

    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        /** 인증받기 버튼 누르면 입력한 전화번호로 인증번호 발송 */
        binding.btnPhone.setOnClickListener {
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) { }
                override fun onVerificationFailed(e: FirebaseException) {
                }
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@SignupUserActivity.verificationId = verificationId
                    Log.e(TAG, "$token")
                }
            }

            val phone = "+8210${binding.edtPhone2.text}${binding.edtPhone3.text}"

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("$phone")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

            binding.btnPhoneCheck.isEnabled = true
        }

        /** 번호확인 버튼 누르면 입력한 인증번호 확인 후 토스트 메시지 */
        binding.btnPhoneCheck.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, "${binding.edtPhoneCheck.text}")
            signInWithPhoneAuthCredential(credential)
        }

        /** 핸드폰 두번째값 : 모두 입력시 세번째값으로 포커스 넘김 && 둘,셋 번호 길이 8이면 인증받기 버튼 활성화 */
        binding.edtPhone2.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 4) {
                    binding.edtPhone3.requestFocus()
                }
                binding.btnPhone.isEnabled = (text!!.length + binding.edtPhone3.text.length) == 8
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 핸드폰 세번쨰값 : 둘,셋 번호 길이 8이면 인증받기 버튼 활성화 */
        binding.edtPhone3.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 4) {
                    binding.edtPhone3.requestFocus()
                }
                binding.btnPhone.isEnabled = (text!!.length + binding.edtPhone2.text.length) == 8
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 비밀번호 체크 : password 와 passwordCheck 가 다르면 에러메시지를 띄운다 */
        binding.edtPasswordCheck.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!text!!.equals("111")) {
                    Log.e(TAG, "$text   ${binding.edtPassword.text}")
                    binding.edtPasswordCheck.error = "비밀번호가 다릅니다."
                } else {
                    binding.edtPasswordCheck.error = null
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

        })

        /** 다음 버튼 누르면 강아지 정보 입력 액티비티로 전환 */
        binding.btnNext.setOnClickListener {
            val flag = true
            val userCode = auth.uid
            val phone = "${binding.edtPhone1.text}${binding.edtPhone2}${binding.edtPhone3}"


//            val password = binding.edtPassword.text.toString()
//            val nickname = binding.edtNickname.text.toString()
//
//            val introduce = binding.edtIntroduce.text.toString()
//            val user = User(phone, password, nickname, introduce)
//            val userDAO = UserDAO()
//            userDAO.signUpUser(userCode!!, user)
//
            val intent = Intent(this@SignupUserActivity, SignupPuppyActivity::class.java)
            intent.putExtra("userCode", userCode)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_right_enter,R.anim.activity_right_exit)
            finish()
        }
    }

    /** 툴바 백버튼 누르면 로그인 액티비티로 돌아감 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this@SignupUserActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** 핸드폰 인증을 위한 펑션 */
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "핸드폰 인증을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "핸드폰 인증 성공")//인증성공
                    binding.btnNext.isEnabled = true
                    binding.btnPhone.isEnabled = false
                    binding.btnPhoneCheck.isEnabled = false
                    binding.edtPhone1.isEnabled = false
                    binding.edtPhone2.isEnabled = false
                    binding.edtPhone3.isEnabled = false
                }
                else {
                    Toast.makeText(this, "핸드폰 인증 번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "핸드폰 인증 실패")//인증실패
                }
            }
    }
}
