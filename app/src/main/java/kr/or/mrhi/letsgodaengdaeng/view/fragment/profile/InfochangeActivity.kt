package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityInfochangeBinding

class InfochangeActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfochangeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfochangeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}