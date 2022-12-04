package kr.or.mrhi.letsgodaengdaeng.view.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogCommentBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogTwoBinding
import kr.or.mrhi.letsgodaengdaeng.view.activity.BottomSheetDialog


class BottomSheetDialogComment : BottomSheetDialogFragment() {

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
        return binding.root
    }
}