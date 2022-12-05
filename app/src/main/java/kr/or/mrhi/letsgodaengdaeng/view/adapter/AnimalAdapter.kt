package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.dataClass.Animal
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemAnimalBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper

class AnimalAdapter (val context: Context, val animalList: MutableList<Animal>, val dbHelper: DBHelper): RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnimalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val animal = animalList[position]

        binding.tvName.text = animal.name
        binding.tvAge.text = animal.age
        binding.tvGender.text = animal.gender
        binding.tvBreeds.text = animal.breeds

        Glide.with(context)
            .load("https://${dbHelper.selectOnePhoto(animal.num!!)}")
            .circleCrop()
            .into(binding.ivAnimalImage)
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    class ViewHolder(val binding: ItemAnimalBinding): RecyclerView.ViewHolder(binding.root)
}