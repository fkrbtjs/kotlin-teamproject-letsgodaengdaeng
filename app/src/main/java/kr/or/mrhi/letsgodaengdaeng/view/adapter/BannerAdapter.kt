package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemBannerBinding

class BannerAdapter (val context: Context, val bannerList: MutableList<Int>): RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val banner = bannerList[position]

        Glide.with(context)
            .load(banner)
            .fitCenter()
            .into(binding.imageView)

        binding.root.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            when (position) {
                0 -> {
                    intent.data = Uri.parse("https://animal.seoul.go.kr/adopt")
                }
                1 -> {
                    intent.data = Uri.parse("https://animal.seoul.go.kr/animalplay")
                }
                2 -> {
                    intent.data = Uri.parse("http://koreananimals.or.kr/notice")
                }
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    class ViewHolder(val binding: ItemBannerBinding): RecyclerView.ViewHolder(binding.root)
}