package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySignupUserBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SignupUserActivity : AppCompatActivity() {
    lateinit var binding: kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySignupUserBinding

    val TAG = this.javaClass.simpleName
    val auth = Firebase.auth
    var verificationId: String? = null
    var passwordFlag = false
    var passwordCheckFlag = false
    var nicknameFlag = false
    var user: User? = null
    var userImageUri: Uri? = null
    var requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        auth.signOut()

        Listener()

        requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .into(binding.ivUserImage)
            }
            userImageUri = it.data?.data
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
                    Toast.makeText(this@SignupUserActivity, "핸드폰 인증을 성공하였습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(TAG, "핸드폰 인증 성공")//인증성공
                    binding.btnNext.isEnabled = true
                    binding.btnPhone.isEnabled = false
                    binding.btnPhoneCheck.isEnabled = false
                    binding.edtPhone1.isEnabled = false
                    binding.edtPhone2.isEnabled = false
                    binding.edtPhone3.isEnabled = false
                    binding.edtPhoneCheck.isEnabled = false
                } else {
                    Toast.makeText(this, "인증 번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "핸드폰 인증 실패")//인증실패
                }
            }
    }

    /** 리스너 모음 */
    fun Listener() {

        /** 유저 프로필 이미지 등록 */
        binding.ivUserImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        /** 이미지 길게 누르면 등록된 이미지를 디폴트이미지로 변경 */
        binding.ivUserImage.setOnLongClickListener {
            Glide.with(this@SignupUserActivity)
                .load(R.drawable.default_person)
                .into(binding.ivUserImage)
            userImageUri = null
            return@setOnLongClickListener true
        }

        /** 인증받기 버튼 누르면 입력한 전화번호로 인증번호 발송 */
        binding.btnPhone.setOnClickListener {
            val userDAO = UserDAO()
            var phone = "010${binding.edtPhone2.text}${binding.edtPhone3.text}"
            /** 데이터를 한번만 받고 끝내기 위해서 Single 쓴다 */
            userDAO.selectUserType("phone", phone)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value != null) {
                            binding.btnPhoneCheck.isEnabled = false
                            binding.edtPhoneCheck.text.clear()
                            binding.edtPhone2.text.clear()
                            binding.edtPhone3.text.clear()
                            softkeyboardHide(binding.edtPhone3)
                            Toast.makeText(this@SignupUserActivity, "이미 가입된 전화번호 입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            val callbacks =
                                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
                                    override fun onVerificationFailed(e: FirebaseException) {
                                    }

                                    override fun onCodeSent(
                                        verificationId: String,
                                        token: PhoneAuthProvider.ForceResendingToken
                                    ) {
                                        this@SignupUserActivity.verificationId = verificationId
                                        Log.e(TAG, "$token")
                                    }
                                }
                            phone = "+8210${binding.edtPhone2.text}${binding.edtPhone3.text}"
                            val options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("$phone")
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(this@SignupUserActivity)
                                .setCallbacks(callbacks)
                                .build()
                            PhoneAuthProvider.verifyPhoneNumber(options)
                            binding.btnPhoneCheck.isEnabled = true
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "btnPhone $error")
                    }
                })
        }

        /** 번호확인 버튼 누르면 입력한 인증번호 확인 후 토스트 메시지 */
        binding.btnPhoneCheck.setOnClickListener {
            val credential =
                PhoneAuthProvider.getCredential(verificationId!!, "${binding.edtPhoneCheck.text}")
            signInWithPhoneAuthCredential(credential)
        }

        /** 핸드폰 두번째값 : 모두 입력시 세번째값으로 포커스 넘김 && 둘,셋 번호 길이 8이면 인증받기 버튼 활성화 , 키보드 다운  */
        binding.edtPhone2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 4) {
                    binding.edtPhone3.requestFocus()
                }
                if ((text!!.length + binding.edtPhone3.text.length) == 8) {
                    binding.btnPhone.isEnabled = true
                    softkeyboardHide(binding.edtPhone2)
                } else {
                    binding.btnPhone.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 핸드폰 세번쨰값 : 둘,셋 번호 길이 8이면 인증받기 버튼 활성화 , 키보드 다운 */
        binding.edtPhone3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 4) {
                    binding.edtPhone3.requestFocus()
                }
                if ((text!!.length + binding.edtPhone2.text.length) == 8) {
                    binding.btnPhone.isEnabled = true
                    softkeyboardHide(binding.edtPhone3)
                } else {
                    binding.btnPhone.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 핸드폰 인증번호 입력시 에러메시지 삭제 */
        binding.edtPhoneCheck.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tilPhoneCheck.isErrorEnabled = false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 비밀번호 체크 : password 와 passwordCheck 가 다르면 에러메시지를 띄운다 */
        binding.edtPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.toString() == binding.edtPassword.text.toString()) {
                    binding.tilPasswordCheck.isErrorEnabled = false
                    passwordCheckFlag = true
                } else {
                    binding.tilPasswordCheck.error = "비밀번호를 확인해주세요"
                    passwordCheckFlag = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {}
        })

        /** 비밀번호 패턴 체크 */
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text!!.matches("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*]).{4,10}$".toRegex())) {
                    binding.tilPassword.isErrorEnabled = false
                    passwordFlag = true
                } else {
                    binding.tilPassword.error = "영문,숫자,특수문자 두가지 이상 , 4자 이상 입력해주세요"
                    passwordFlag = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 닉네임 패턴 체크 */
        binding.edtNickname.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text!!.matches("^[가-힣a-zA-Z0-9]{2,10}$".toRegex())) {
                    binding.tilNickname.isErrorEnabled = false
                    binding.btnNicknameCheck.isEnabled = true
                } else {
                    binding.tilNickname.error = "문자나 숫자로만 2자 이상 입력해주세요"
                    binding.btnNicknameCheck.isEnabled = false
                }
                nicknameFlag = false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 닉네임 중복 체크 */
        binding.btnNicknameCheck.setOnClickListener {
            val userDAO = UserDAO()
            userDAO.selectUserType("nickname", binding.edtNickname.text.toString())
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value != null) {
                            nicknameFlag = false
                            binding.edtNickname.requestFocus()
                            binding.tilNickname.error = "이미 있는 닉네임입니다."
                        } else {
                            nicknameFlag = true
                            Toast.makeText(
                                this@SignupUserActivity,
                                "사용가능한 닉네임입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            softkeyboardHide(binding.edtNickname)
                            binding.btnNicknameCheck.isEnabled = false
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "btnNicknameCheck $error")
                    }
                })
        }

        /** 다음 버튼 누르면 강아지 정보 입력 액티비티로 전환 */
        binding.btnNext.setOnClickListener {
            if (nicknameFlag && passwordFlag && passwordCheckFlag) {
                softkeyboardHide(binding.edtIntroduce)

                val userCode = auth.uid
                val phone =
                    "${binding.edtPhone1.text}${binding.edtPhone2.text}${binding.edtPhone3.text}"

                val password = binding.edtPassword.text.toString()
                val nickname = binding.edtNickname.text.toString()
                val introduce = binding.edtIntroduce.text.toString()
                user = User(phone, password, nickname, introduce)

                val intent = Intent(this@SignupUserActivity, SignupPuppyActivity::class.java)
                intent.putExtra("userCode", userCode)
                intent.putExtra("user", user)
                if (userImageUri != null) {
                    intent.putExtra("userImageUri", userImageUri)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.activity_right_enter, R.anim.activity_right_exit)
                finish()
            } else {
                Toast.makeText(this@SignupUserActivity, "입력 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** 키보드 숨김 */
    fun softkeyboardHide(editText: EditText) {
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    /** 핸드폰 인증을 하면 파이어베이스에 유저가 생성된다(Authentication 식별자와 uid)
    그렇기때문에 정보를 입력하지 않고 창을 닫아버렸을때 그 생성된 식별자를 삭제한다 SignupPuppy 에도 동일 기능있음 */
    override fun onDestroy() {
        super.onDestroy()
        if (user == null) {
            auth.currentUser?.delete()
        }
    }
}
