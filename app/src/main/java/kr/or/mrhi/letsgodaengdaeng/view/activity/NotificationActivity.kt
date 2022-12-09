package kr.or.mrhi.letsgodaengdaeng.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    lateinit var binding : ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
    }
}