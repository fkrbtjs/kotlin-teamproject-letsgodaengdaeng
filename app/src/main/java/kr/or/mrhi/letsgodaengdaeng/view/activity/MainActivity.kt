package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityMainBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.TabMainBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.fragment.CommunityFragment
import kr.or.mrhi.letsgodaengdaeng.view.fragment.HomeFragment
import kr.or.mrhi.letsgodaengdaeng.view.fragment.ProfileFragment
import kr.or.mrhi.letsgodaengdaeng.view.fragment.WalkFragment

class MainActivity : AppCompatActivity() {
    companion object{
        var userCode: String? = null
        lateinit var userInfo: User
    }

    lateinit var binding: ActivityMainBinding
    lateinit var homeFragment: HomeFragment
    lateinit var communityFragment: CommunityFragment
    lateinit var walkFragment: WalkFragment
    lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFragment = HomeFragment()
        communityFragment = CommunityFragment()
        walkFragment = WalkFragment()
        profileFragment = ProfileFragment()

        userCode = intent.getStringExtra("userCode")
        val userDAO = UserDAO()
        userDAO.selectUser(userCode!!)?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                userInfo = User("${user?.phone}", "${user?.password}", "${user?.nickname}","${user?.introduce}","${user?.address}")
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        addTab()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "홈" -> changeFragment("홈")
                    "커뮤니티" -> changeFragment("커뮤니티")
                    "산책하기" -> changeFragment("산책하기")
                    "프로필" -> changeFragment("프로필")
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        
        changeFragment("홈")
    }

    fun changeFragment(title: String){

        var tabIndex : TabLayout.Tab?
        //탭레이아웃 탭을 강제로 이동시킨다
        when(title){
            "홈" -> {
                tabIndex = binding.tabLayout.getTabAt(0)
                binding.tabLayout.selectTab(tabIndex)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, homeFragment)
                    .commit()
            }
            "커뮤니티" -> {
                tabIndex = binding.tabLayout.getTabAt(1)
                binding.tabLayout.selectTab(tabIndex)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, communityFragment)
                    .commit()
            }
            "산책하기" -> {
                tabIndex = binding.tabLayout.getTabAt(2)
                binding.tabLayout.selectTab(tabIndex)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, walkFragment)
                    .commit()
            }
            "프로필" -> {
                tabIndex = binding.tabLayout.getTabAt(4)
                binding.tabLayout.selectTab(tabIndex)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, profileFragment)
                    .commit()
            }
        }
    }

    fun addTab() {
        binding.tabLayout.apply {
            val tab1: TabLayout.Tab = binding.tabLayout.newTab()
            val tab2: TabLayout.Tab = binding.tabLayout.newTab()
            val tab3: TabLayout.Tab = binding.tabLayout.newTab()
            val tab4: TabLayout.Tab = binding.tabLayout.newTab()

            tab1.text = "홈"
            tab2.text = "커뮤니티"
            tab3.text = "산책하기"
            tab4.text = "프로필"

            tab1.customView = createTabView(tab1)
            tab2.customView = createTabView(tab2)
            tab3.customView = createTabView(tab3)
            tab4.customView =  createTabView(tab4)

            addTab(tab1)
            addTab(tab2)
            addTab(tab3)
            addTab(tab4)
        }
    }

    fun createTabView(tab: TabLayout.Tab): View {
        val tabMainBinding = TabMainBinding.inflate(layoutInflater)
        tabMainBinding.tvTabName.text = tab.text
        when (tab.text) {
            "홈" -> tabMainBinding.ivTabLogo.setImageResource(R.drawable.ic_home)
            "커뮤니티" -> tabMainBinding.ivTabLogo.setImageResource(R.drawable.ic_community)
            "산책하기" -> tabMainBinding.ivTabLogo.setImageResource(R.drawable.ic_paw)
            "프로필" -> tabMainBinding.ivTabLogo.setImageResource(R.drawable.ic_dog)
        }
        return tabMainBinding.root
    }
}