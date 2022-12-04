package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityWriteBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import kr.or.mrhi.letsgodaengdaeng.view.adapter.GalleryAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {

    lateinit var binding : ActivityWriteBinding
    lateinit var category : String
    lateinit var filePath: String
    var imageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemList = listOf("카테고리 선택", "친구해요", "공유해요", "궁금해요")
        val adapter = ArrayAdapter(this, R.layout.item_spinner, itemList)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) Toast.makeText(this@WriteActivity, itemList[position], Toast.LENGTH_SHORT).show()
                category = itemList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btnBack.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            intent.putExtra("community","commmunity")
//            overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
            finish()
        }


        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .apply(RequestOptions().override(500, 500))
                    .centerCrop()
                    .into(binding.ivPicture)
            }

            imageUri = it.data?.data
            var cursor = contentResolver.query(
                it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA),
                null,
                null,
                null
            )
            cursor?.moveToFirst().let {
                filePath = cursor!!.getString(0)
            }
        }



        //사진 가져오기 버튼눌렀을때 갤러리에서 사진가져오기
        binding.btnGetPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        //등록버튼눌렀을때 파이어베이스에 정보 저장
        binding.tvComplete.setOnClickListener {
            val communityDAO = CommunityDAO()
            val content = binding.edtContent.text.toString()
            val date = SimpleDateFormat("yy-MM-dd").format(Date())
            val nickname = MainActivity.userInfo.nickname!!
            //firebase realtime database diary 테이블에 입력할때 가져오는 key
            val key = communityDAO.databaseReference?.push()?.key
            val community = CommunityVO(key,nickname,"성동구",date,category,content,"0","0")

            //firebase storage 이미지를 업로드 경로명 셋팅한다.
            val imageReference = communityDAO.storage?.reference?.child("images/${community.docID}.jpg")
            Log.d("pictureurl","${imageReference}")
            val file = Uri.fromFile(File(filePath))
            imageReference?.putFile(imageUri!!)?.addOnSuccessListener {
                Log.d("community", "imageReference?.putFile Success")
                Toast.makeText(this,"스토리지저장성공!",Toast.LENGTH_SHORT).show()
                //firebase realtime database diary 테이블에 입력
                communityDAO.databaseReference?.child(key!!)?.setValue(community)?.addOnSuccessListener {
                    Log.d("community", "community 테이블 입력 성공")
                }?.addOnFailureListener {
                    Log.e("community", "community 테이블 입력 실패 $it")
                }
            }?.addOnFailureListener {
                Log.e("community", "imageReference?.putFile Fail $it")
                Toast.makeText(this,"스토리지저장실패!",Toast.LENGTH_SHORT).show()
            }
            finish()
        }

    }

}