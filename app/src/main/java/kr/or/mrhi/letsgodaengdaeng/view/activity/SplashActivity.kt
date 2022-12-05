package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySplashBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity.Companion.DB_NAME
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity.Companion.VERSION

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this, DB_NAME, VERSION)

        if (dbHelper.selectAnimal() != null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, SeouldataActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}