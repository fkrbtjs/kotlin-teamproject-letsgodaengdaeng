package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ivUpdate.setOnClickListener{
            val intent = Intent(this, UpdateActivity::class.java)
            startActivity(intent)
        }
    }


    //뒤로가기버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}