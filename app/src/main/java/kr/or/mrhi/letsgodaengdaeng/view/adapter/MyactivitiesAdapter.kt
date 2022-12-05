package kr.or.mrhi.letsgodaengdaeng.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitiesItemBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class MyactivitiesAdapter(val context: Context, val activitiesList: MutableList<CommunityVO>): RecyclerView.Adapter<MyactivitiesAdapter.ActivitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesViewHolder {
        val binding = ActivitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivitiesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
        val binding = holder.binding
        val activities = activitiesList.get(position)

        val userDAO = UserDAO()
        val communityDAO = CommunityDAO()
        communityDAO.selectCommunity2("NIMbmC76PYt624gfeKe")?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val community: CommunityVO? = snapshot.getValue(CommunityVO::class.java)
                    binding.tvMyCategory.text = community?.category
                    binding.tvWriting.text = community?.content
                    binding.tvReviewCount.text = community?.commentCount.toString()
                    Log.d("letsgodaengdaeng", "select success")
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    class ActivitiesViewHolder(val binding: ActivitiesItemBinding): RecyclerView.ViewHolder(binding.root)
}