package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.SeoulGil
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentHomeViewBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.activity.SeouldataActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.AnimalAdapter
import kr.or.mrhi.letsgodaengdaeng.view.adapter.SeoulGilAdapter
import kr.or.mrhi.letsgodaengdaeng.view.adapter.VeterinaryAdapter

class HomeViewFragment : Fragment() {

    lateinit var seoulgilAdapter: SeoulGilAdapter
    private var seoulgilList: MutableList<SeoulGil> = mutableListOf<SeoulGil>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeViewBinding.inflate(inflater, container, false)

        val dbHelper =
            DBHelper(requireContext(), SeouldataActivity.DB_NAME, SeouldataActivity.VERSION)

        val veterinaryList = dbHelper.selectVeterinary()
        val veterinaryAdapter = VeterinaryAdapter(requireContext(), veterinaryList!!, dbHelper)
        binding.rvVeterinary.adapter = veterinaryAdapter
        binding.rvVeterinary.layoutManager =
            LinearLayoutManager(requireContext())

        val animalList = dbHelper.selectAnimal()
        val animalAdapter = AnimalAdapter(requireContext(), animalList!!, dbHelper)
        binding.rvAnimal.adapter = animalAdapter
        binding.rvAnimal.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        seoulgilAdapter = SeoulGilAdapter(requireContext(), seoulgilList)
        binding.rvSeoulgil.adapter = seoulgilAdapter
        binding.rvSeoulgil.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        /** 모든 서울길 정보를 보여준다 */
        dbHelper.selectSeoulGil("all")?.let { seoulgilList.addAll(it) }

        /** 버튼을 눌렀을 때 해당 지역의 서울길 정보만 보여준다 */
        binding.btnAll.setOnClickListener { filterLocal("all") }
        binding.btnGwanak.setOnClickListener { filterLocal("관악구") }
        binding.btnJongro.setOnClickListener { filterLocal("종로구") }
        binding.btnJungku.setOnClickListener { filterLocal("중구") }
        binding.btnSungbuk.setOnClickListener { filterLocal("성북구") }

        return binding.root
    }

    fun filterLocal(gu: String) {
        val dbHelper =
            DBHelper(requireContext(), SeouldataActivity.DB_NAME, SeouldataActivity.VERSION)
        seoulgilList.clear()
        dbHelper.selectSeoulGil(gu)?.let { seoulgilList.addAll(it) }
        seoulgilAdapter.notifyDataSetChanged()
    }

}