package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    companion object {
        val DB_NAME = "test"
        var VERSION = 1
    }

    private val TAG = this.javaClass.simpleName
    private var userCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**************************************************************************************/

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupUserActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}
