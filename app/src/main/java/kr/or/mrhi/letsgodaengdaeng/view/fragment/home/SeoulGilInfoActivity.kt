package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.or.mrhi.letsgodaengdaeng.dataClass.Animal
import kr.or.mrhi.letsgodaengdaeng.dataClass.SeoulGil
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySeoulGilInfoBinding

class SeoulGilInfoActivity : AppCompatActivity() {

    lateinit var binding : ActivitySeoulGilInfoBinding
    var seoulGil: SeoulGil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeoulGilInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        seoulGil = intent.getParcelableExtra("seoulGil")

        binding.tvCourseName.setText(seoulGil?.name)
        binding.tvName.text = seoulGil?.name
        binding.tvLocal.text = seoulGil?.local
        binding.tvDistance.text = seoulGil?.distance
        binding.tvTime.text = seoulGil?.time
        binding.tvLevel.text = seoulGil?.courseLevel
        binding.tvDetailCourse.text = seoulGil?.detailCourse
        binding.tvContent.text = seoulGil?.content




    }
}