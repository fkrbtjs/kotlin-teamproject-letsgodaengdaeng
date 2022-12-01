package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemImagesBinding


class GalleryAdapter(val context: Context, val imageList: MutableList<Uri>): RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
            Glide.with(context)
                .load(imageList[position])
                .apply(RequestOptions().override(500,500))
                .into(holder.binding.ivGallery)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class GalleryViewHolder(val binding: ItemImagesBinding): RecyclerView.ViewHolder(binding.root)
}