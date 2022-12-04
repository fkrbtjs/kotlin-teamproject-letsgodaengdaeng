package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommentVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemCommentBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemMainBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommentDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.CommentActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.dialog.BottomSheetDialogComment
import kr.or.mrhi.letsgodaengdaeng.view.dialog.BottomSheetDialogTwo

class CommentAdapter (val context: Context, val commentList: MutableList<CommentVO>): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentDAO = CommentDAO()
        val comment = commentList[position]
        val binding = holder.binding
        binding.tvNickname.text = comment.nickname
        binding.tvDate.text = comment.date
        binding.tvLocal.text = comment.local
        binding.tvContent.text = comment.content
        val userImgRef = commentDAO.storage!!.reference.child("userImage/${comment.userID}.jpg")
        Log.d("userImgRef","${userImgRef}")
        userImgRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(context)
                    .load(it.result)
                    .circleCrop()
                    .into(binding.ivProfilePicture)
            }
        }
        if (comment.userID.equals(MainActivity.userCode)){
            binding.btnMore.visibility = View.VISIBLE
        }
//        binding.btnMore.setOnClickListener {
//            val bottomSheetDialogComment = BottomSheetDialogComment()
//            bottomSheetDialogComment.show(,bottomSheetDialogComment.tag)
//        }

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    class CommentViewHolder(val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root)
}