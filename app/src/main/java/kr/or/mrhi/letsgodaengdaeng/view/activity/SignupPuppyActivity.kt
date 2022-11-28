package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySignupPuppyBinding

class SignupPuppyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupPuppyBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnSignup.setOnClickListener {
            val userCode = intent.getStringExtra("userCode")
            val name = binding.edtName.text.toString()
            val gender = binding.edtGender.text.toString()
            val breed = binding.edtBreed.text.toString()
            val size = binding.edtSize.text.toString()
            val tendency = binding.edtTendency.text.toString()
            val puppy = Puppy(name, gender, breed, size, tendency)

        }
    }
}

//class Puppy(val name: String? = null, val gender: String? = null,
//            val breed: String? = null, val size: String? = null,
//            val tendency: String? = null, val picture: String? = null)