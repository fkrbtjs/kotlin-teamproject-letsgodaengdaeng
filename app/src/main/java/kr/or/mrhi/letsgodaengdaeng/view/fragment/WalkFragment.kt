package kr.or.mrhi.letsgodaengdaeng.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentWalkBinding
import kr.or.mrhi.letsgodaengdaeng.view.fragment.walk.WalkMapActivity

class WalkFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWalkBinding.inflate(layoutInflater, container, false)

        binding.btnWalk.setOnClickListener {
            val intent = Intent(requireContext(), WalkMapActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }


}