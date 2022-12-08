package kr.or.mrhi.letsgodaengdaeng.view.fragment.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentAllBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentShareBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.CustomAdapter
import kr.or.mrhi.letsgodaengdaeng.view.fragment.CommunityFragment


class ShareFragment : Fragment() {
    lateinit var binding : FragmentShareBinding
    lateinit var adapter: CustomAdapter
    var communityList: MutableList<CommunityVO> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        selectShare()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShareBinding.inflate(inflater, container, false)

        communityList = mutableListOf()
        adapter = CustomAdapter(requireContext(),communityList)
        val linearLayout = LinearLayoutManager(context)
        linearLayout.reverseLayout = true
        linearLayout.stackFromEnd = true
        binding.recyclerviewShare.layoutManager = linearLayout
        binding.recyclerviewShare.adapter = adapter

        selectShare()


        return binding.root
    }

    fun selectShare() {
        val communityDAO = CommunityDAO()
        val communityFragment = (context as MainActivity).communityFragment
        communityDAO.selectShareCommunity()?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                communityList.clear()
                for (userdata in snapshot.children) {
                    //json 방식으로 넘어오기 때문에 클래스 방식으로 변환해야함
                    val community = userdata.getValue(CommunityVO::class.java)
                    //비어있던 userKey 부분에 key 값을 넣어준다
                    community?.docID = userdata.key.toString()
                    if (community != null) {
                        if (communityFragment.type == 0) {
                            communityList.add(community)
                        } else {
                            if (community.local.equals(MainActivity.userInfo.address)) {
                                communityList.add(community)
                            }
                        }
                    }
                }// end of for
                adapter.notifyDataSetChanged()
            }// end of onDataChange

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                Log.e("firebasecrud22", "selectUser() ValueEventListener cancel $error")
            }
        })
    }
}