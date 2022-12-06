package kr.or.mrhi.letsgodaengdaeng.view.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogTwoBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.UpdateCommentActivity

class BottomSheetDialogTwo(val community : CommunityVO ) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBottomSheetDialogTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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