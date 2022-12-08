package kr.or.mrhi.letsgodaengdaeng.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySearchBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.adapter.CustomAdapter

class SearchActivity : AppCompatActivity() {

    lateinit var binding : ActivitySearchBinding
    lateinit var communityList: MutableList<CommunityVO>
    lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
            finish()
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
        }

        communityList = mutableListOf()
        adapter = CustomAdapter(this,communityList)
        val linearLayout = LinearLayoutManager(this)
        linearLayout.reverseLayout = true
        linearLayout.stackFromEnd = true
        binding.searchRecyclerView.layoutManager = linearLayout
        binding.searchRecyclerView.adapter = adapter

        /** 서치뷰의 텍스트가 바뀔때마다 해당 텍스트의 컨텐츠들을 찾아 보여줌 */
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query.isNullOrBlank()){
                    communityList.clear()
                    adapter.notifyDataSetChanged()
                }else{
                    Log.d("query","${query}")
                    communityList.clear()
                    val communityDAO = CommunityDAO()
                    communityDAO.selectCommunity()?.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (userdata in snapshot.children) {
                                //json 방식으로 넘어오기 때문에 클래스 방식으로 변환해야함
                                val community = userdata.getValue(CommunityVO::class.java)
                                //비어있던 userKey 부분에 key 값을 넣어준다
                                community?.docID = userdata.key.toString()
                                if (community != null) {
                                    if (community?.content.toString().contains("$query") || community?.nickname.toString().contains("$query")){
                                        communityList.add(community)
                                        Log.d("query","${community}")
                                    }
                                }
                            }// end of for
                            adapter.notifyDataSetChanged()
                        }// end of onDataChange

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("query", "selectUser() ValueEventListener cancel $error")
                        }
                    })
                }
               return true
            }
        })

    }
}