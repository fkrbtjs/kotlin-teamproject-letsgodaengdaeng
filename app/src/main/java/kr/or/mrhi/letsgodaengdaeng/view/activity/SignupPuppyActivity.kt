package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySignupPuppyBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.PuppyDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO

class SignupPuppyActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupPuppyBinding

    val TAG = this.javaClass.simpleName
    var nameFlag = false
    var breedFlag = false
    var tendencyFlag = false
    var puppy: Puppy? = null
    var puppyImageUri = Uri.parse("android.resource://kr.or.mrhi.letsgodaengdaeng/${R.drawable.ic_dog}")
    var requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPuppyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()

        requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .into(binding.ivPuppyPicture)
            }
            puppyImageUri = it.data?.data
        }
    }

    /** 리스너 모음 */
    fun listener() {

        /** 퍼피 프로필 이미지 등록 */
        binding.ivPuppyPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        /** 이미지 길게 누르면 등록된 이미지를 디폴트이미지로 변경 */
        binding.ivPuppyPicture.setOnLongClickListener {
            Glide.with(this@SignupPuppyActivity)
                .load(R.drawable.default_person)
                .into(binding.ivPuppyPicture)
            puppyImageUri = null
            return@setOnLongClickListener true
        }

        /** 화면이 그려질때 라디오버튼 기본 체크값 */
        binding.rbtnGenderMale.isChecked = true
        binding.rbtnSizeLarge.isChecked = true

        /** 이름 패턴 체크 */
        binding.edtName.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text!!.matches("^[가-힣a-zA-Z]{1,10}$".toRegex())){
                    binding.tilName.isErrorEnabled = false
                    nameFlag = true
                } else {
                    binding.tilName.error = "문자만 입력해주세요"
                    nameFlag = false
                }
                valueCheck()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 견종 패턴 체크 */
        binding.edtBreed.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text!!.matches("^[가-힣a-zA-Z]{1,10}$".toRegex())){
                    binding.tilBreed.isErrorEnabled = false
                    breedFlag = true
                } else {
                    binding.tilBreed.error = "문자만 입력해주세요"
                    breedFlag = false
                }
                valueCheck()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 성향 패턴 체크 */
        binding.edtTendency.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text!!.matches("^[가-힣a-zA-Z]{1,10}$".toRegex())){
                    binding.tilTendency.isErrorEnabled = false
                    tendencyFlag = true
                } else {
                    binding.tilTendency.error = "문자만 입력해주세요"
                    tendencyFlag = false
                }
                valueCheck()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        /** 회원가입 완료버튼 */
        binding.btnSignup.setOnClickListener {
            val userCode = intent.getStringExtra("userCode")
            val name = binding.edtName.text.toString()
            var gender: String? = null
            gender = if (binding.rbtnGenderMale.isChecked) {
                "남아"
            } else {
                "여아"
            }

            val breed = binding.edtBreed.text.toString()
            var size: String? = null
            size = if (binding.rbtnSizeLarge.isChecked) {
                "대형견"
            } else if (binding.rbtnSizeMedium.isChecked) {
                "중형견"
            } else {
                "소형견"
            }

            val tendency = binding.edtTendency.text.toString()

            /** 유저 정보 */
            val user = intent.getParcelableExtra<User>("user")
            val userDAO = UserDAO()
            /** 유저 이미지 */
            var userImageUri: Uri? = null
            if (intent.hasExtra("userImageUri")) {
                userImageUri = intent.getParcelableExtra("userImageUri")
            }

            /** 퍼피 정보 */
            puppy = Puppy(name, gender, breed, size, tendency)
            val puppyDAO = PuppyDAO()
            /** 유저 이미지 = puppyImageUri */

            /** 파이어베이스 리얼타임 & 스토리지 저장 */
            userDAO.storage?.reference?.child("userImage/$userCode.jpg")?.putFile(userImageUri!!)?.addOnSuccessListener {
            }?.addOnFailureListener {
                Log.e(TAG, "putFile(userImageUri) $it")
            }
            puppyDAO.storage?.reference?.child("puppyImage/$userCode.jpg")?.putFile(puppyImageUri!!)?.addOnSuccessListener {
            }?.addOnFailureListener {
                Log.e(TAG, "putFile(userImageUri) $it")
            }

            userDAO.signUpUser(userCode!!, user)
            puppyDAO.signUpPuppy(userCode!!, puppy!!)

            val intent = Intent(this@SignupPuppyActivity, MainActivity::class.java)
            intent.putExtra("userCode", userCode)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
            finish()

        }
    }

    /** 플래그 확인 후 가입 버튼 활성화 */
    fun valueCheck() {
        binding.btnSignup.isEnabled = nameFlag && breedFlag && tendencyFlag
    }

    /** 핸드폰 인증을 하면 파이어베이스에 유저가 생성된다(Authentication 식별자와 uid)
        그렇기때문에 정보를 입력하지 않고 창을 닫아버렸을때 그 생성된 식별자를 삭제한다 SignupUser 에도 동일 기능있음 */
    override fun onDestroy() {
        super.onDestroy()
        val auth = Firebase.auth
        if (puppy == null) {
            auth.currentUser?.delete()
        }
    }
}
