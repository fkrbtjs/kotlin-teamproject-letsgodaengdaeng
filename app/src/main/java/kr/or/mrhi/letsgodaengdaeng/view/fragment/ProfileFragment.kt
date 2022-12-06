package kr.or.mrhi.letsgodaengdaeng.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommentVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentProfileBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.PuppyDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.InfoActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.MyCommentActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.MyreviewActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.SettingActivity

class ProfileFragment : Fragment() {
    companion object {
        var communityList: MutableList<CommunityVO> = mutableListOf()
        var commentList: MutableList<CommentVO> = mutableListOf()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        val puppy = Puppy()
        val communityDAO = CommunityDAO()

        communityList.clear()
        commentList.clear()

        /** 모든 커뮤니티 docID를 받은 후 docID를 참조해 모든 글에서 내 유저코드로 내가 쓴 댓글을 찾는다 */
        communityDAO.selectCommunity()?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userdata in snapshot.children) {
                    val community = userdata.getValue(CommunityVO::class.java)

                    if (community?.userID == MainActivity.userCode) {
                        if (community != null) {
                            communityList.add(community)
                            Log.d("dasdas","${community}")
                        }
                    }
                    communityDAO.selectMyComment("${userdata.key}", MainActivity.userCode!!)?.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (userdata in snapshot.children) {
                                val comment = userdata.getValue(CommentVO::class.java)
                                if (comment != null) {
                                    commentList.add(comment)
                                }
                            }
                            binding.tvMycommentCount.setText(commentList.size.toString())
                        }// end of onDataChange
                        override fun onCancelled(error: DatabaseError) {
                            Log.e("letsgodaengdaeng", "selectMyComment ValueEventListener cancel $error")
                        }
                    })
                }// end of for
                binding.tvMyreviewCount.setText(communityList.size.toString())
            }// end of onDataChange
            override fun onCancelled(error: DatabaseError) {
<<<<<<< HEAD
                Log.e("letsgodaengdaeng", "selectMyComment ValueEventListener cancel $error")
=======
                Toast.makeText(context, "가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                Log.e("letsgodaengdaeng", "selectUser() ValueEventListener cancel $error")
>>>>>>> woong
            }
        })

        /**firebase storage 에서 사진 가져오기.**/
        val userDAO = UserDAO()
        /**firebase storage 이미지 경로를 알려준다*/
        val img = userDAO.storage!!.reference.child("images/.png")

        img.downloadUrl.addOnCompleteListener{
            if(it.isSuccessful){
                Glide.with(container!!.context)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }

        binding.writing.setOnClickListener{
            val intent = Intent(context, MyreviewActivity::class.java)
            startActivity(intent)
        }

        binding.ivSetting.setOnClickListener{
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.review.setOnClickListener {
            val intent = Intent(context, MyCommentActivity::class.java)
            startActivity(intent)
        }
        
        binding.cdIdCard.setOnClickListener{
            val intent = Intent(context, InfoActivity::class.java)
            intent.putExtra("userID",MainActivity.userCode)
            startActivity(intent)
        }

        val puppyDAO = PuppyDAO()
        val puppyImg = puppyDAO.storage!!.reference.child("puppyImage/${MainActivity.userCode}.jpg")
        puppyImg.downloadUrl.addOnCompleteListener{
            if(it.isSuccessful){
                Glide.with(container!!.context)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }

        puppyDAO.selectPuppy(MainActivity.userCode!!)?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val puppy: Puppy? = snapshot.getValue(Puppy::class.java)
                    binding.tvName.text = puppy?.name
                    binding.tvGender.text = puppy?.gender
                    binding.tvBreed.text = puppy?.breed
                    binding.tvTendency.text = puppy?.tendency
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return binding.root
    }
}