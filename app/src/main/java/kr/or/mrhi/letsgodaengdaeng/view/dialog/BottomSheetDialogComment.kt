package kr.or.mrhi.letsgodaengdaeng.view.dialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommentVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogCommentBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogTwoBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.BottomSheetDialog
import java.util.HashMap


class BottomSheetDialogComment(val docID : String, val commentID : String, val commentList: MutableList<CommentVO>) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBottomSheetDialogCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetDialogCommentBinding.inflate(layoutInflater, container, false)

        val communityDAO = CommunityDAO()

        binding.tvDelete.setOnClickListener {
            Log.d("commentList.size","${commentList.size}")
            communityDAO.deleteComment(docID,commentID)
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["commentCount"] = commentList.size -1
            Log.d("commentList.size","${commentList.size}")
            communityDAO.updateCommentCount(docID,hashMap)
            dismiss()
        }

        binding.tvCancle.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}