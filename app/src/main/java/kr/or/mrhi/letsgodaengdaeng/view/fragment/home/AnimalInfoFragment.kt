package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.combine
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentAnimalInfoBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity.Companion.DB_NAME
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity.Companion.VERSION

class AnimalInfoFragment : Fragment() {
    var animalInfoActivity: AnimalInfoActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        animalInfoActivity = context as AnimalInfoActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAnimalInfoBinding.inflate(inflater, container, false)

        val animal = animalInfoActivity?.animal

        binding.tvName.text = animal?.name
        binding.tvAge.text = animal?.age
        binding.tvGender.text = animal?.gender
        binding.tvBreeds.text = animal?.breeds
        binding.tvWeight.text = animal?.weight
        binding.tvInfo.text = animal?.intro
        binding.tvInfo.setLinkTextColor(R.color.rosy_brown)

        val dbHelper = DBHelper(animalInfoActivity, DB_NAME, VERSION)

        Glide.with(requireActivity())
            .load("https://${dbHelper.selectOnePhoto(animal?.num!!)}")
            .circleCrop()
            .into(binding.ivAnimalImage)

        return binding.root
    }

}

