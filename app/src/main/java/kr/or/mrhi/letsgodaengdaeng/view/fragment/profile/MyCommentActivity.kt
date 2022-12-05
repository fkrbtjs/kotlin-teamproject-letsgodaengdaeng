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
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityMyCommentBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.MyCommentAdapter

class MyCommentActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyCommentBinding
    lateinit var commentList: MutableList<CommentVO>
    lateinit var adapter: MyCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.MyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        commentList = mutableListOf()
        adapter = MyCommentAdapter(this,commentList,MainActivity.userCode!!)
        val linearLayout = LinearLayoutManager(this)
        linearLayout.reverseLayout = true
        linearLayout.stackFromEnd = true
        binding.recyclerview.layoutManager = linearLayout
        binding.recyclerview.adapter = adapter

        selectUser()

    }

    private fun selectUser() {
        val communityDAO = CommunityDAO()
        communityDAO.selectCommunity4(MainActivity.userCode!!)?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear()

                val userinfo = HashMap<String,String>()
                for (userdata in snapshot.children) {
                    //json 방식으로 넘어오기 때문에 클래스 방식으로 변환해야함
                    val comment = userdata.getValue(CommentVO::class.java)

                    if (comment != null) {
                        commentList.add(comment)
                    }
                }// end of for
                adapter.notifyDataSetChanged()
            }// end of onDataChange

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyCommentActivity, "가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                Log.e("firebasecrud22", "selectUser() ValueEventListener cancel $error")
            }
        })
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