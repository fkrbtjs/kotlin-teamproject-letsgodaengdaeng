package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemMainBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.CommentActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.profile.InfoActivity

class MyactivitiesAdapter(val context: Context, val communityList: MutableList<CommunityVO>): RecyclerView.Adapter<MyactivitiesAdapter.MyActivityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyactivitiesAdapter.MyActivityViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyactivitiesAdapter.MyActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyactivitiesAdapter.MyActivityViewHolder, position: Int) {
        var likeFlag = 0
        val community = communityList[position]
        val binding = holder.binding
        binding.tvNickname.text = community.nickname
        binding.tvDate.text = community.date
        binding.tvCategory.text = community.category
        binding.tvLocal.text = community.local
        binding.tvContent.text = community.content
        binding.tvCommentCount.text = community.commentCount.toString()
        //사진을 firebase storage에서 가져와야된다.(경로를 지정해서 : data.docID)
        val communityDAO = CommunityDAO()
        //firebase storage에서 저장되어있는 이미지 경로 나타냄
        val imgRef = communityDAO.storage!!.reference.child("images/${community.docID}.jpg")
        val userImgRef = communityDAO.storage!!.reference.child("userImage/${community.userID}.jpg")
        userImgRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(context)
                    .load(it.result)
                    .circleCrop()
                    .into(binding.ivProfilePicture)
            }
        }


        imgRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(context)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }

        binding.root.setOnClickListener {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra("communityCode","${community.docID}")
            ContextCompat.startActivity(binding.linearComment.context,intent,null)
            (holder.itemView.context as Activity).overridePendingTransition(
                R.anim.slide_right_enter,
                R.anim.slide_right_exit)
        }

        binding.ivProfilePicture.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java)
            intent.putExtra("userID",community.userID)
            ContextCompat.startActivity(binding.ivProfilePicture.context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return communityList.size
    }

    class MyActivityViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
}