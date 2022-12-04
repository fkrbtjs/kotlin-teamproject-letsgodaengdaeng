package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemMainBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.CommentActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class CustomAdapter(val context: Context, val communityList: MutableList<CommunityVO>): RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var likeFlag = -1
        val community = communityList[position]
        val binding = holder.binding
        binding.tvNickname.text = community.nickname
        binding.tvDate.text = community.date
        binding.tvCategory.text = community.category
        binding.tvLocal.text = community.local
        binding.tvContent.text = community.content
        binding.tvLikesCount.text = community.likeCount


        //사진을 firebase storage에서 가져와야된다.(경로를 지정해서 : data.docID)
        val communityDAO = CommunityDAO()
        //firebase storage에서 저장되어있는 이미지 경로 나타냄
        val imgRef = communityDAO.storage!!.reference.child("images/${community.docID}.jpg")

        imgRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(context)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }
        binding.linearLikes.setOnClickListener {

        }

        binding.linearComment.setOnClickListener {
            val intent = Intent(context,CommentActivity::class.java)
            intent.putExtra("communityCode","${community.docID}")
            ContextCompat.startActivity(binding.linearComment.context,intent,null)
        }

    }

    override fun getItemCount(): Int {
        return communityList.size
    }

    class CustomViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
}