package kr.or.mrhi.letsgodaengdaeng.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommentVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityCommentBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommentDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.adapter.CommentAdapter
import kr.or.mrhi.letsgodaengdaeng.view.adapter.CustomAdapter
import kr.or.mrhi.letsgodaengdaeng.view.dialog.BottomSheetDialogTwo
import java.text.SimpleDateFormat
import java.util.*


class CommentActivity : AppCompatActivity() {

    lateinit var binding : ActivityCommentBinding
    lateinit var community: CommunityVO
    lateinit var commentList: MutableList<CommentVO>
    lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val commentDAO = CommentDAO()

        commentList = mutableListOf()
        commentAdapter = CommentAdapter(this,commentList)
        val linearLayout = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = linearLayout
        binding.recyclerview.adapter = commentAdapter

        val communityCode = intent.getStringExtra("communityCode")
        val communityDAO = CommunityDAO()


        communityDAO.selectCommunity2(communityCode!!)?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                community = snapshot.getValue(CommunityVO::class.java)!!
                binding.tvCategory.setText(community.category)
                binding.tvNickname.setText(community.nickname)
                binding.tvDate.setText(community.date)
                binding.tvLocal.setText(community.local)
                binding.tvContent.setText(community.content)

                val imgRef = communityDAO.storage!!.reference.child("images/${community.docID}.jpg")
                imgRef.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful){
                        Glide.with(applicationContext)
                            .load(it.result)
                            .into(binding.ivPicture)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        commentDAO.selectComment(communityCode)?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear()
                for (userdata in snapshot.children) {
                    //json 방식으로 넘어오기 때문에 클래스 방식으로 변환해야함
                    val commentData = userdata.getValue(CommentVO::class.java)
                    //비어있던 userKey 부분에 key 값을 넣어준다
                    commentData?.docID = userdata.key.toString()
                    if (commentData != null) {
                        commentList.add(commentData)
                    }
                }// end of for
                commentAdapter.notifyDataSetChanged()
            }// end of onDataChange

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                Log.e("firebasecrud22", "selectUser() ValueEventListener cancel $error")
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnMore.setOnClickListener {
            val bottomSheetDialogTwo = BottomSheetDialogTwo()
            bottomSheetDialogTwo.show(supportFragmentManager,bottomSheetDialogTwo.tag)
        }



        binding.btnSend.setOnClickListener {
            val docID = MainActivity.userCode
            val nickname = MainActivity.userInfo.nickname!!
            val local = community.local
            val date = SimpleDateFormat("yy-MM-dd").format(Date())
            val content = binding.edtComment.text.toString()
            val comment = CommentVO(docID,communityCode,nickname,local,date,content)
            commentDAO.databaseReference?.push()?.setValue(comment)?.addOnSuccessListener {
                Log.d("comment", "comment 테이블 입력 성공")
            }?.addOnFailureListener {
                Log.e("comment", "comment 테이블 입력 실패 $it")
            }
            binding.edtComment.text.clear()

        }


    }
}