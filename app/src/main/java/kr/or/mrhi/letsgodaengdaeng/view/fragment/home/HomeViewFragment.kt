package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentHomeViewBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.AnimalAdapter

class HomeViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeViewBinding.inflate(inflater, container, false)

        val dbHelper = DBHelper(requireContext(), SeouldataActivity.DB_NAME, SeouldataActivity.VERSION)

        val animalList = dbHelper.selectAnimal()
        val animalAdapter = AnimalAdapter(requireContext(), animalList!!, dbHelper)
        binding.rvAnimal.adapter = animalAdapter
        binding.rvAnimal.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }
}