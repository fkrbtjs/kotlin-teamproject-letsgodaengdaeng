package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityInfochangeBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class InfochangeActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfochangeBinding
    var nicknameFlag = false
    var passwordFlag = false
    var passwordCheckFlag = false
    var imageUri: Uri? = null
    val userDAO = UserDAO()
    lateinit var filePath: String
    var userPassword : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfochangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolChange)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /**유저 이미지 등록*/
        val userImg = userDAO.storage!!.reference.child("userImage/${MainActivity.userCode}.jpg")
        userImg.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Glide.with(applicationContext)
                    .load(it.result)
                    .into(binding.ivUserImage)
            }
        }
        /** 버튼이 눌렸을때 유저의 비밀번호가 일치하면 회원정보 변경 화면을 보여준다.*/
        binding.btnSuccess.setOnClickListener {
            userDAO.selectUser(MainActivity.userCode!!)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User? = snapshot.getValue(User::class.java)
                        if(user?.password == binding.edtPassword.text.toString()){
                            Toast.makeText(this@InfochangeActivity, "확인 되었습니다.", Toast.LENGTH_SHORT).show()
                            binding.scChangeView.visibility = View.VISIBLE
                            binding.llUserCheck.visibility = View.INVISIBLE
                            userPassword = user.password
                        }else{
                            Toast.makeText(this@InfochangeActivity, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("letsgodaengdaeng", "${error.toString()}")
                    }
                })
        }

        /** 저장된 유저 정보 불러온다*/
        userDAO.selectUser(MainActivity.userCode!!)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userData in snapshot.children) {
                    val user: User? = snapshot.getValue(User::class.java)
                    binding.edtNickname.setText("${user?.nickname}")
                    binding.edtIntroduce.setText("${user?.introduce}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        /** 내 앨범 목록 불러오기*/
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop() //사진을 자르지 않음
                    .into(binding.ivUserImage)
                imageUri = it.data?.data
                var cursor = contentResolver.query(
                    it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA),
                    null,
                    null,
                    null
                )
                cursor?.moveToFirst().let {
                    filePath = cursor!!.getString(0)
                }
            }
        }// end of requestLauncher

        binding.ivUserImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        /** 이미지 길게 누르면 등록된 이미지를 디폴트이미지로 변경 */
        binding.ivUserImage.setOnLongClickListener {
            Glide.with(this@InfochangeActivity)
                .load(R.drawable.default_person)
                .into(binding.ivUserImage)
            imageUri = null
            return@setOnLongClickListener true
        }

        /**유저 정보 업데이트*/
        binding.btnUserUpdate.setOnClickListener {

            val nickName = binding.edtNickname.text.toString()
            val introduce = binding.edtIntroduce.text.toString()

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["nickname"] = nickName

            /** 패스워드를 변경하지 않을경우 기존 패스워드로 저장한다.*/
            if(binding.edtCheckUpdate.text.isEmpty()){
                hashMap["password"] = userPassword!!
            }else{
                hashMap["password"] = binding.edtCheckUpdate.text.toString()
            }
            hashMap["introduce"] = introduce

            userDAO.updateUser(MainActivity.userCode!!, hashMap).addOnSuccessListener {
                Log.d("letsgodaengdaeng", "user update success")
                finish()
            }.addOnFailureListener {
                Log.d("letsgodaengdaeng", "user update fail")
            }
            if (imageUri != null) {
                val updateUserImg =
                    userDAO.storage?.reference?.child("userImage/${MainActivity.userCode}.jpg")
                updateUserImg?.putFile(imageUri!!)?.addOnSuccessListener {
                    Log.d("letsgodaengdaeng", "Success")
                }?.addOnFailureListener {
                    Log.d("letsgodaengdaeng", "Fail")
                }
            }
        }

        /** 비밀번호 체크 : password 와 passwordCheck 가 다르면 에러메시지를 띄운다 */
        binding.edtCheckUpdate.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.toString() == binding.edtPasswordUpdate.text.toString()) {
                    binding.tilCheckUpdate.isErrorEnabled = false
                    passwordCheckFlag = true
                } else {
                    binding.tilCheckUpdate.error = "비밀번호를 확인해주세요"
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
                                this@InfochangeActivity,
                                "사용가능한 닉네임입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            softkeyboardHide(binding.edtNickname)
                            binding.btnNicknameCheck.isEnabled = false
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("letsgodaengdaeng", "btnNicknameCheck $error")
                    }
                })
        }
    }

    /** 키보드 숨김 */
    fun softkeyboardHide(editText: EditText) {
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    /** 뒤로가기버튼*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}