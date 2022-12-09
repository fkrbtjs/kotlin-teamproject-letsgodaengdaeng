package kr.or.mrhi.letsgodaengdaeng.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentCommunityBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.UsertabButtonBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.BottomSheetDialog
import kr.or.mrhi.letsgodaengdaeng.view.activity.NotificationActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.SearchActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.WriteActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.PagerAdapter
import kr.or.mrhi.letsgodaengdaeng.view.fragment.community.AllFragment
import kr.or.mrhi.letsgodaengdaeng.view.fragment.community.FriendFragment
import kr.or.mrhi.letsgodaengdaeng.view.fragment.community.QuestionFragment
import kr.or.mrhi.letsgodaengdaeng.view.fragment.community.ShareFragment

class CommunityFragment : Fragment() {

    lateinit var binding: FragmentCommunityBinding
    lateinit var allFragment: AllFragment
    lateinit var friendFragment: FriendFragment
    lateinit var questionFragment: QuestionFragment
    lateinit var shareFragment: ShareFragment
    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        /** 탭 생성 */
        val tab1 : TabLayout.Tab = binding.cmTabLayout.newTab()
        tab1.text = "전체보기"
        val tab2 : TabLayout.Tab = binding.cmTabLayout.newTab()
        tab2.text = "친구해요"
        val tab3 : TabLayout.Tab = binding.cmTabLayout.newTab()
        tab3.text = "공유해요"
        val tab4 : TabLayout.Tab = binding.cmTabLayout.newTab()
        tab4.text = "궁금해요"

        binding.cmTabLayout.addTab(tab1)
        binding.cmTabLayout.addTab(tab2)
        binding.cmTabLayout.addTab(tab3)
        binding.cmTabLayout.addTab(tab4)

        /** 탭,뷰페이저 연결 */
        val pagerAdapter = PagerAdapter(this)
        val title = mutableListOf<String>("전체보기","친구해요","공유해요","궁금해요")
        allFragment = AllFragment()
        friendFragment = FriendFragment()
        questionFragment = QuestionFragment()
        shareFragment = ShareFragment()

        pagerAdapter.addFragment(allFragment,title[0])
        pagerAdapter.addFragment(friendFragment,title[1])
        pagerAdapter.addFragment(shareFragment,title[2])
        pagerAdapter.addFragment(questionFragment,title[3])
        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.isSaveEnabled = false

        TabLayoutMediator(binding.cmTabLayout,binding.viewPager){ tab, position->
            tab.setCustomView(createTabView(title[position]))
        }.attach()

        /** 지역범위 설정 버튼 */
        binding.cmBtnSettings.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog()
            bottomSheetDialog.show(parentFragmentManager,bottomSheetDialog.tag)
        }

        /** 게시글작성창 플로팅버튼 */
        binding.fltWrite.setOnClickListener {
            val intent = Intent(requireContext(), WriteActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit)
        }

        /** 검색창 버튼 */
        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit)
        }

        return binding.root
    }

    fun changeFragmentAll(){
        binding.viewPager.currentItem = 0
    }

    private fun createTabView(title: String): View {
        val userTabBinding = UsertabButtonBinding.inflate(layoutInflater)
        userTabBinding.tvName.text = title
        return userTabBinding.root
    }
}