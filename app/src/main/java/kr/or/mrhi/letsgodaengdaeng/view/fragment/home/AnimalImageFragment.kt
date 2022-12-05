package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.AnimalPhoto
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentAnimalImageBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentAnimalInfoBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity.Companion.DB_NAME
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity.Companion.VERSION
import kr.or.mrhi.letsgodaengdaeng.view.adapter.AnimalAdapter
import kr.or.mrhi.letsgodaengdaeng.view.adapter.AnimalPhotoAdapter


class AnimalImageFragment : Fragment() {
    var animalInfoActivity: AnimalInfoActivity? = null
    var animalImageList = mutableListOf<AnimalPhoto>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        animalInfoActivity = context as AnimalInfoActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAnimalImageBinding.inflate(inflater, container, false)

        val dbHelper = DBHelper(animalInfoActivity, DB_NAME, VERSION)

        val animal = animalInfoActivity?.animal

        animalImageList.clear()
        animalImageList = dbHelper.selectAllPhoto(animal?.num!!)!!
        Log.e("animalImageList", "${animalImageList.size}")
        val animalPhotoAdapter = AnimalPhotoAdapter(requireContext() , animalImageList!!)
        binding.recyclerView.adapter = animalPhotoAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())



        return binding.root
    }
}