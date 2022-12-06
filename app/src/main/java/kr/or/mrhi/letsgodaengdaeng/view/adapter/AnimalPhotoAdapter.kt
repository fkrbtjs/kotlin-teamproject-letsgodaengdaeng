package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.dataClass.Animal
import kr.or.mrhi.letsgodaengdaeng.dataClass.AnimalPhoto
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemAnimalBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemAnimalImageBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.fragment.home.AnimalInfoActivity

class AnimalPhotoAdapter (val context: Context, val animalPhotoList: MutableList<AnimalPhoto>): RecyclerView.Adapter<AnimalPhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnimalImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val animal = animalPhotoList[position]

        Log.e("https://${animal.photo}","https://${animal.photo}")

        Glide.with(context)
            .load("https://${animal.photo}")
            .into(binding.ivAnimalImage)

    }

    override fun getItemCount(): Int {
        return animalPhotoList.size
    }

    class ViewHolder(val binding: ItemAnimalImageBinding): RecyclerView.ViewHolder(binding.root)
}