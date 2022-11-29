package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySignupPuppyBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.PuppyDAO

class SignupPuppyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupPuppyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val userCode = intent.getStringExtra("userCode")

            val name = binding.edtName.text.toString()

            var gender: String? = null
            if (binding.rbtnGenderMale.isChecked) {
                gender = "남아"
            } else {
                gender = "여아"
            }

            val breed = binding.edtBreed.text.toString()

            var size: String? = null
            if (binding.rbtnSizeLarge.isChecked) {
                size = "대형견"
            } else if (binding.rbtnSizeMedium.isChecked) {
                size = "중형견"
            } else {
                size = "소형견"
            }


            val tendency = binding.edtTendency.text.toString()

            val puppy = Puppy(name, gender, breed, size, tendency)

            val puppyDAO = PuppyDAO()
            puppyDAO.signUpPuppy(userCode!!, puppy)

            val intent = Intent(this@SignupPuppyActivity, MainActivity::class.java)
            intent.putExtra("userCode", "userCode")
            startActivity(intent)
            overridePendingTransition(R.anim.activity_right_enter, R.anim.activity_right_exit)
            finish()
        }
    }
}