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
import kr.or.mrhi.letsgodaengdaeng.view.adapter.SeoulGilAdapter
import kr.or.mrhi.letsgodaengdaeng.view.adapter.VeterinaryAdapter

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

        val veterinaryList = dbHelper.selectVeterinary()
        val veterinaryAdapter = VeterinaryAdapter(requireContext(), veterinaryList!!, dbHelper)
        binding.rvVeterinary.adapter = veterinaryAdapter
        binding.rvVeterinary.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val animalList = dbHelper.selectAnimal()
        val animalAdapter = AnimalAdapter(requireContext(), animalList!!, dbHelper)
        binding.rvAnimal.adapter = animalAdapter
        binding.rvAnimal.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val seoulgilList =  dbHelper.selectSeoulGil()
        val seoulgilAdapter = SeoulGilAdapter(requireContext(),seoulgilList!!,dbHelper)
        binding.rvSeoulgil.adapter = seoulgilAdapter
        binding.rvSeoulgil.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        return binding.root
    }
}