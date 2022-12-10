package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.dataClass.Walk
import kr.or.mrhi.letsgodaengdaeng.dataClass.WalkMarker
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemAlbumBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemWalkBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.fragment.ProfileFragment

class WalkAdapter(val context: Context, val walkList: MutableList<Walk>) :
    RecyclerView.Adapter<WalkAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemWalkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        val walk = walkList[position]

        binding.tvDate.text = walk.date
        binding.tvTime.text = walk.time
        binding.tvPoint.text = walk.point
    }

    override fun getItemCount(): Int {
        return walkList.size
    }

    class CustomViewHolder(val binding: ItemWalkBinding) : RecyclerView.ViewHolder(binding.root)
}