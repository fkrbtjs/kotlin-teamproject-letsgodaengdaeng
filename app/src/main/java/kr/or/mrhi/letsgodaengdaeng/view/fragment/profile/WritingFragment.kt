package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentWritingBinding
import kr.or.mrhi.letsgodaengdaeng.view.adapter.MyactivitiesAdapter

class WritingFragment : Fragment() {
    lateinit var communityVO: CommunityVO
    lateinit var activitiesList: MutableList<CommunityVO>
    lateinit var myActivitiesAdapter: MyactivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWritingBinding.inflate(inflater, container, false)

        // 리사이클러뷰 레이아웃 결정
        val layoutManager = LinearLayoutManager(container?.context)
        //어뎁터 제공
        myActivitiesAdapter = MyactivitiesAdapter(requireContext(), activitiesList)
        //연결
        binding.writingRecyclerView.layoutManager = layoutManager
        binding.writingRecyclerView.adapter = myActivitiesAdapter


        return binding.root
    }

}