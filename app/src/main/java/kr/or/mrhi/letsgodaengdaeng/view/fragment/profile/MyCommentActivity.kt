package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommentVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityMyCommentBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.MyCommentAdapter
import kr.or.mrhi.letsgodaengdaeng.view.fragment.ProfileFragment

class MyCommentActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyCommentBinding
    lateinit var adapter: MyCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.MyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val communityDAO = CommunityDAO()


        var commentList = ProfileFragment.commentList
        adapter = MyCommentAdapter(this,commentList)
        val linearLayout = LinearLayoutManager(this)
        linearLayout.reverseLayout = true
        linearLayout.stackFromEnd = true
        binding.recyclerview.layoutManager = linearLayout
        binding.recyclerview.adapter = adapter

//        selectMyComment()
    }

//    /** 모든 커뮤니티 docID를 받은 후 docID를 참조해 모든 글에서 내 유저코드로 내가 쓴 댓글을 찾는다 */
//    fun selectMyComment() {
//        val communityDAO = CommunityDAO()
//        communityDAO.selectCommunity()?.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (userdata in snapshot.children) {
//                    val community = userdata.getValue(CommunityVO::class.java)
//                    community?.docID = userdata.key.toString()
//
//                    communityDAO.selectMyComment(community?.docID!!,
//                        MainActivity.userCode!!)?.addValueEventListener(object: ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (userdata in snapshot.children) {
//                                val comment = userdata.getValue(CommentVO::class.java)
//                                if (comment != null) {
//                                    commentList.add(comment)
//                                }
//                            }
//                            adapter.notifyDataSetChanged()
//                        }// end of onDataChange
//                        override fun onCancelled(error: DatabaseError) {
//                            Log.e("letsgodaengdaeng", "selectMyComment ValueEventListener cancel $error")
//                        }
//                    })
//                }// end of for
//            }// end of onDataChange
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("letsgodaengdaeng", "selectMyComment ValueEventListener cancel $error")
//            }
//        })
//    }

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