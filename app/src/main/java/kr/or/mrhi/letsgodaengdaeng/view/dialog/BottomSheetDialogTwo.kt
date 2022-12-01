package kr.or.mrhi.letsgodaengdaeng.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentBottomSheetDialogTwoBinding


class BottomSheetDialogTwo : BottomSheetDialogFragment() {

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

        return binding.root
    }
}