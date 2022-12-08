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
        val friendFragment = (context as MainActivity).communityFragment.friendFragment
        val shareFragment = (context as MainActivity).communityFragment.shareFragment
        val questionFragment = (context as MainActivity).communityFragment.questionFragment

        binding.btnDongnae.setOnClickListener {

            communityFragment.type = 1
//            communityFragment.changeFragmentAll()

            dismiss()
        }

        binding.btnAll.setOnClickListener {

            communityFragment.type = 0
//            communityFragment.changeFragmentAll()

            dismiss()
        }

        return binding.root
    }
}