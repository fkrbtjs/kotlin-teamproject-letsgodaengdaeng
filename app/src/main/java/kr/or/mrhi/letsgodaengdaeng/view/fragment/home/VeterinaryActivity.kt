package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.Veterinary
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityVeterinaryBinding

class VeterinaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityVeterinaryBinding
    var veterinary: Veterinary? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVeterinaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolVeterinary)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        veterinary = intent.getParcelableExtra("veterinary")

        binding.tvHosName.text = veterinary?.name
        binding.tvAddress.text = veterinary?.address
        binding.tvPhone.text = veterinary?.phone
        binding.tvPhone.setLinkTextColor(R.color.rosy_brown)
    }

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