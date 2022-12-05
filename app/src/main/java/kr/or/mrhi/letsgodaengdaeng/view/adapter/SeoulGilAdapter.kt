package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.letsgodaengdaeng.dataClass.SeoulGil
import kr.or.mrhi.letsgodaengdaeng.databinding.ItemSeoulgilBinding
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import kr.or.mrhi.letsgodaengdaeng.view.fragment.home.SeoulGilInfoActivity

class SeoulGilAdapter  (val context: Context, val seoulGilList: MutableList<SeoulGil>, val dbHelper: DBHelper): RecyclerView.Adapter<SeoulGilAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSeoulgilBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val seoulGil = seoulGilList[position]

        binding.tvName.text = seoulGil.name
        binding.tvLocal.text = seoulGil.local
        binding.tvDistance.text = seoulGil.distance
        binding.tvLevel.text = seoulGil.courseLevel


        binding.root.setOnClickListener {
            val intent = Intent(context, SeoulGilInfoActivity::class.java)
            intent.putExtra("seoulGil", seoulGil)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return seoulGilList.size
    }

    class ViewHolder(val binding: ItemSeoulgilBinding): RecyclerView.ViewHolder(binding.root)
}