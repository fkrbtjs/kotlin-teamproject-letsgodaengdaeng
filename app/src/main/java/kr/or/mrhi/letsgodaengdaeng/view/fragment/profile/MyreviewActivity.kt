package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityMyreviewBinding
import kr.or.mrhi.letsgodaengdaeng.view.adapter.MyactivitiesAdapter
import kr.or.mrhi.letsgodaengdaeng.view.fragment.ProfileFragment

class MyreviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyreviewBinding
    lateinit var adapter: MyactivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.MyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var communityList = ProfileFragment.communityList
        adapter = MyactivitiesAdapter(this,communityList)
        val linearLayout = LinearLayoutManager(this)
        linearLayout.reverseLayout = true
        linearLayout.stackFromEnd = true
        binding.recyclerview.layoutManager = linearLayout
        binding.recyclerview.adapter = adapter

    }
    
    /**백버튼을 눌렀을떄 이동할 경로 지정*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}