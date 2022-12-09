package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommentVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityCommentBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommentDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.adapter.CommentAdapter
import kr.or.mrhi.letsgodaengdaeng.view.dialog.BottomSheetDialogTwo
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.InfoActivity
import java.text.SimpleDateFormat
import java.util.*

class CommentActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName

    lateinit var binding : ActivityCommentBinding
    lateinit var community: CommunityVO
    lateinit var commentList: MutableList<CommentVO>
    lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val communityCode = intent.getStringExtra("communityCode")
        val communityDAO = CommunityDAO()

        commentList = mutableListOf()
        commentAdapter = CommentAdapter(this,commentList,communityCode!!)
        val linearLayout = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = linearLayout
        binding.recyclerview.adapter = commentAdapter

        /** 게시글 정보 가져오기 */
        communityDAO.selectCommunityID(communityCode!!)?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue(CommunityVO::class.java) != null){
                    community = snapshot.getValue(CommunityVO::class.java)!!
                    binding.tvCategory.setText(community.category)
                    binding.tvNickname.setText(community.nickname)
                    binding.tvDate.setText(community.date)
                    binding.tvLocal.setText(community.local)
                    binding.tvContent.setText(community.content)
                    binding.tvCommentCount.setText(community.commentCount.toString())
                    if(community.userID.toString().equals(MainActivity.userCode.toString())){
                        binding.btnMore.visibility = View.VISIBLE
                    }
                    val userImgRef = communityDAO.storage!!.reference.child("userImage/${community.userID}.jpg")
                    userImgRef.downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful){
                            Glide.with(applicationContext)
                                .load(it.result)
                                .circleCrop()
                                .into(binding.ivProfilePicture)
                        }
                    }
                    val imgRef = communityDAO.storage!!.reference.child("images/${community.docID}.jpg")
                    imgRef.downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful){
                            Glide.with(applicationContext)
                                .load(it.result)
                                .into(binding.ivPicture)
                        }
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })

        /** 게시글의 댓글 가져오기 */
        communityDAO.selectComment(communityCode)?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear()
                for (userdata in snapshot.children) {
                    //json 방식으로 넘어오기 때문에 클래스 방식으로 변환해야함
                    val commentData = userdata.getValue(CommentVO::class.java)
                    if (commentData != null) {
                        commentList.add(commentData)
                    }
                } // end of for
                commentAdapter.notifyDataSetChanged()
            }// end of onDataChange
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "selectUser() ValueEventListener cancel $error")
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)

        }
        binding.ivProfilePicture.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("userID",community.userID)
            ContextCompat.startActivity(binding.ivProfilePicture.context,intent,null)
        }

        binding.btnMore.setOnClickListener {
            val bottomSheetDialogTwo = BottomSheetDialogTwo(community)
            bottomSheetDialogTwo.show(supportFragmentManager,bottomSheetDialogTwo.tag)
        }

        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        /** 댓글 등록 */
        binding.btnSend.setOnClickListener {
            val commentDAO = CommentDAO()
            val userID = MainActivity.userCode
            val nickname = MainActivity.userInfo.nickname!!
            val local = community.local
            val date = SimpleDateFormat("yy-MM-dd").format(Date())
            val content = binding.edtComment.text.toString()
            val commentID = commentDAO.databaseReference?.push()?.key
            val comment = CommentVO(commentID,userID,communityCode,nickname,local,date,content)

            communityDAO.databaseReference?.child(communityCode)?.child("comment")?.child(commentID!!)?.setValue(comment)?.addOnSuccessListener {
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["commentCount"] = commentList.size
                communityDAO.updateCommentCount(community.docID!!,hashMap)
            }?.addOnFailureListener {
                Log.e(TAG, "comment 테이블 입력 실패 $it")
            }
            binding.edtComment.text.clear()
            inputMethodManager.hideSoftInputFromWindow(binding.edtComment.windowToken, 0)

        }
        if(intent.hasExtra("content")) {
            for (i in 0 until commentList.size) {
                if (commentList[i].content == intent.getStringExtra("content")!!) {
                    binding.recyclerview.smoothScrollToPosition(i)
                }
            }
        }

    }
}