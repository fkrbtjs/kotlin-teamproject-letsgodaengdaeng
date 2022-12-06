package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityMyCommentBinding
import kr.or.mrhi.letsgodaengdaeng.view.adapter.MyCommentAdapter
import kr.or.mrhi.letsgodaengdaeng.view.fragment.ProfileFragment

class MyCommentActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyCommentBinding
    lateinit var adapter: MyCommentAdapter
    var commentList = ProfileFragment.commentList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.MyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = MyCommentAdapter(this,commentList)
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