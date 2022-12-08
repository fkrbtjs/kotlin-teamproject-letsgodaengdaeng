package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemAlbumBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.fragment.ProfileFragment

class AlbumAdapter(val context: Context, val albumList: MutableList<String>) :
    RecyclerView.Adapter<AlbumAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        val album = albumList[position]
        val communityDAO = CommunityDAO()

        val image = communityDAO.storage!!.reference.child("images/${album}.jpg")
        Log.d("album",album)
        image.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(context)
                    .load(it.result)
                    .into(binding.ivAlbum)
            }
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    class CustomViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)
}