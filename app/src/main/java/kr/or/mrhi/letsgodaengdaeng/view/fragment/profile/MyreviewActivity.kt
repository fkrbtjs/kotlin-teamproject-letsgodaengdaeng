package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityMyreviewBinding
import java.time.format.TextStyle

class MyreviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyreviewBinding
    lateinit var writingFragment: WritingFragment
    lateinit var reviewFragment: ReviewFragment
    lateinit var niceFragment: NiceFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.MyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        writingFragment = WritingFragment()
        reviewFragment = ReviewFragment()
        niceFragment = NiceFragment()

        addTab()
        binding.tabMyReview.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "내가 쓴 글" -> changeFragment("내가 쓴 글")
                    "나의 댓글" -> changeFragment("나의 댓글")
                    "좋아요" -> changeFragment("좋아요")

                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    fun changeFragment(title: String) {

        var tabIndex: TabLayout.Tab?
        //탭레이아웃 탭을 강제로 이동시킨다
        when (title) {
            "내가 쓴 글" -> {
                tabIndex = binding.tabMyReview.getTabAt(0)
                binding.tabMyReview.selectTab(tabIndex)
            }
            "나의 댓글" -> {
                tabIndex = binding.tabMyReview.getTabAt(1)
                binding.tabMyReview.selectTab(tabIndex)
            }
            "좋아요" -> {
                tabIndex = binding.tabMyReview.getTabAt(2)
                binding.tabMyReview.selectTab(tabIndex)
            }
        }
    }

    fun addTab() {
        binding.tabMyReview.apply {
            val tab1: TabLayout.Tab = binding.tabMyReview.newTab()
            val tab2: TabLayout.Tab = binding.tabMyReview.newTab()
            val tab3: TabLayout.Tab = binding.tabMyReview.newTab()

            tab1.text = "내가 쓴 글"
            tab2.text = "나의 댓글"
            tab3.text = "좋아요"

            addTab(tab1)
            addTab(tab2)
            addTab(tab3)
        }
    }

    /**백버튼을 눌렀을떄 이동할 경로 지정*/
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