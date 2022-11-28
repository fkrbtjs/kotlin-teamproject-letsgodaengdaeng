package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySignupUserBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO

class SignupUserActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("")

        binding.edtIntroduce.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }
        })

        binding.btnNext.setOnClickListener {
            val userCode = intent.getStringExtra("userCode")
            val id = binding.edtID.text.toString()
            val password = binding.edtPassword.text.toString()
            val nickname = binding.edtNickname.text.toString()
            val phone = binding.edtPhone.text.toString()
            val introduce = binding.edtIntroduce.text.toString()
            val user = User(id, password, nickname, phone, introduce)
            val userDAO = UserDAO()
            userDAO.signUpUser(userCode!!, user)

            val intent = Intent(this@SignupUserActivity, SignupPuppyActivity::class.java)
            intent.putExtra("userCode", userCode)
            startActivity(intent)
            finish()
        }
    }

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
}
