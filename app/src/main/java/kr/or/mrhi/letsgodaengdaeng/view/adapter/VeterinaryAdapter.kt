package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.letsgodaengdaeng.dataClass.Veterinary
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemVeterinaryBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.fragment.home.SeoulGilInfoActivity
import kr.or.mrhi.letsgodaengdaeng.view.fragment.home.VeterinaryActivity

class VeterinaryAdapter(val context: Context, val VeterinaryList: MutableList<Veterinary>, val dbHelper: DBHelper): RecyclerView.Adapter<VeterinaryAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemVeterinaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val customViewHolder = CustomViewHolder(binding)
        return customViewHolder
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding
        val veterinary = VeterinaryList[position]

        binding.tvName.text = veterinary.name
        binding.tvAddress.text = veterinary.address
        binding.tvPhone.text = veterinary.phone

        binding.root.setOnClickListener {
            val intent = Intent(context, VeterinaryActivity::class.java)
            intent.putExtra("veterinary", veterinary)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return VeterinaryList.size
    }

    class CustomViewHolder(val binding: ItemVeterinaryBinding) : RecyclerView.ViewHolder(binding.root)
}