package kr.or.mrhi.letsgodaengdaeng.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.dataClass.Walk
import kr.or.mrhi.letsgodaengdaeng.databinding.FragmentWalkBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.WalkDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import kr.or.mrhi.letsgodaengdaeng.view.adapter.BannerAdapter
import kr.or.mrhi.letsgodaengdaeng.view.adapter.WalkAdapter
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

        val walkList = mutableListOf<Walk>()
        val adapter = WalkAdapter(requireActivity(), walkList)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(container?.context)
        recyclerView.adapter = adapter

        val walkDAO = WalkDAO()
        walkDAO.select(MainActivity.userCode!!)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                walkList.clear()
                Log.e("dsadas",snapshot.toString())
                for (walkData in snapshot.children) {
                    val walk = walkData.getValue(Walk::class.java)
                    walkList.add(walk!!)
                    Log.e("dsadas",walk.toString())
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("WalkFragment", "selectUserPhone onCancelled $error")
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

}