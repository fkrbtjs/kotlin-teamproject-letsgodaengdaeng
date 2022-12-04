package kr.or.mrhi.letsgodaengdaeng.view.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.NonDisposableHandle.parent
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogTwoBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommentDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.CommentActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.UpdateCommentActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.WriteActivity


class BottomSheetDialogTwo(val community : CommunityVO ) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBottomSheetDialogTwoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetDialogTwoBinding.inflate(layoutInflater, container, false)

        val communityDAO = CommunityDAO()
        binding.tvCancle.setOnClickListener {
            dismiss()
        }

        binding.tvUpdate.setOnClickListener {
            val intent = Intent(requireContext(), UpdateCommentActivity::class.java)
            intent.putExtra("community",community)

            startActivity(intent)
            dismiss()
        }

        binding.tvDelete.setOnClickListener {
            dismiss()
            requireActivity().finish()
            communityDAO.deleteCommunity(community.docID!!)
        }

        return binding.root
    }
}