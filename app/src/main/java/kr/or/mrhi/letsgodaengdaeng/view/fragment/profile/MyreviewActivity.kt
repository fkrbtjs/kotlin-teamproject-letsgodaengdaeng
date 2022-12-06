package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityMyreviewBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentAllBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.CustomAdapter
import kr.or.mrhi.letsgodaengdaeng.view.adapter.MyactivitiesAdapter
import kr.or.mrhi.letsgodaengdaeng.view.fragment.ProfileFragment
import java.time.format.TextStyle

class MyreviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyreviewBinding
    lateinit var adapter: MyactivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.MyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var communityList = ProfileFragment.communityList
        adapter = MyactivitiesAdapter(this,communityList)
        val linearLayout = LinearLayoutManager(this)
        linearLayout.reverseLayout = true
        linearLayout.stackFromEnd = true
        binding.recyclerview.layoutManager = linearLayout
        binding.recyclerview.adapter = adapter


    }

<<<<<<< HEAD
//    private fun selectUser() {
//        val communityDAO = CommunityDAO()
//        communityDAO.selectCommunity3(MainActivity.userCode!!)?.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                communityList.clear()
//                for (userdata in snapshot.children) {
//                    //json 방식으로 넘어오기 때문에 클래스 방식으로 변환해야함
//                    val community = userdata.getValue(CommunityVO::class.java)
//                    //비어있던 userKey 부분에 key 값을 넣어준다
//                    community?.docID = userdata.key.toString()
//                    if (community != null) {
//                        communityList.add(community)
//                    }
//                }// end of for
//                adapter.notifyDataSetChanged()
//            }// end of onDataChange
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@MyreviewActivity, "가져오기 실패 $error", Toast.LENGTH_SHORT).show()
//                Log.e("firebasecrud22", "selectUser() ValueEventListener cancel $error")
//            }
//        })
//    }
=======
    private fun selectUser() {
        val communityDAO = CommunityDAO()
        communityDAO.selectCommunity3(MainActivity.userCode!!)?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                communityList.clear()
                for (userdata in snapshot.children) {
                    //json 방식으로 넘어오기 때문에 클래스 방식으로 변환해야함
                    val community = userdata.getValue(CommunityVO::class.java)
                    //비어있던 userKey 부분에 key 값을 넣어준다
                    community?.docID = userdata.key.toString()
                    if (community != null) {
                        communityList.add(community)
                    }
                }// end of for
                adapter.notifyDataSetChanged()
            }// end of onDataChange

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyreviewActivity, "가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                Log.e("letsgodaengdaeng", "selectUser() ValueEventListener cancel $error")
            }
        })
    }
>>>>>>> woong

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