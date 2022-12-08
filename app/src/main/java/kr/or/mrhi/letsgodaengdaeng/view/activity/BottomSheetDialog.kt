package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogBinding
import kr.or.mrhi.letsgodaengdaeng.view.fragment.CommunityFragment
import kr.or.mrhi.letsgodaengdaeng.view.fragment.community.AllFragment


class BottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBottomSheetDialogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetDialogBinding.inflate(layoutInflater, container, false)

        val communityFragment = (context as MainActivity).communityFragment
        val allFragment = (context as MainActivity).communityFragment.allFragment

        /** 우리동네 게시물만 보여주기 */
        binding.btnDongnae.setOnClickListener {

            communityFragment.type = 1
            allFragment.selectUser()
            communityFragment.changeFragmentAll()

            dismiss()
        }
        /** 전체동네 게시물 보여주기 */
        binding.btnAll.setOnClickListener {

            communityFragment.type = 0
            allFragment.selectUser()
            communityFragment.changeFragmentAll()

            dismiss()
        }

        return binding.root
    }
}