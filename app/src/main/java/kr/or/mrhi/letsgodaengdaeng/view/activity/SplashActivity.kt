package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}