package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityWriteBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName

    lateinit var binding : ActivityWriteBinding
    lateinit var category : String
    lateinit var filePath: String
    var imageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** 스피너 설정 */
        val itemList = listOf("카테고리 선택", "친구해요", "공유해요", "궁금해요")
        val adapter = ArrayAdapter(this, R.layout.item_spinner, itemList)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = itemList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        /** 뒤로가기 버튼 */
        binding.btnBack.setOnClickListener {
            if(binding.edtContent.text.toString().equals("") && category.equals("카테고리 선택") && imageUri == null){
                finish()
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("작성하신 내용이 사라집니다. 작성화면에서 나가시겠습니까")
                    .setPositiveButton("네",DialogInterface.OnClickListener{ dialog,id->
                        finish()
                        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
                    })
                    .setNegativeButton("아니오",DialogInterface.OnClickListener{dialog , id ->
                    })
                builder.show()
            }
        }

        /** 앨범에서 이미지 받아오기 */
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

        /** 사진 가져오기 버튼 */
        binding.btnGetPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        /** 등록 버튼 */
        binding.tvComplete.setOnClickListener {
            val communityDAO = CommunityDAO()
            val content = binding.edtContent.text.toString()
            val date = SimpleDateFormat("yy-MM-dd").format(Date())
            val nickname = MainActivity.userInfo.nickname!!
            //firebase realtime database diary 테이블에 입력할때 가져오는 key
            val key = communityDAO.databaseReference?.push()?.key

            /** 내용이 없을때 예외처리 */
            if (content.equals("")){
                Toast.makeText(this,"내용을 입력해주세요",Toast.LENGTH_SHORT).show()
            }else{
                /** 카테고리 선택을 안했을 때 예외처리 */
                if(category.equals("카테고리 선택")){
                    Toast.makeText(this,"카테고리를 선택해주세요",Toast.LENGTH_SHORT).show()
                }else{
                    val community = CommunityVO(key,MainActivity.userCode,nickname,MainActivity.userInfo.address,date,category,content,0,0)
                    //firebase storage 이미지를 업로드 경로명 셋팅한다.
                    val imageReference = communityDAO.storage?.reference?.child("images/${community.docID}.jpg")
                    Log.d("pictureurl","${imageReference}")

                    /** 사진을 등록하지 않았을 때 예외처리 */
                    if(imageUri != null){
                        imageReference?.putFile(imageUri!!)?.addOnSuccessListener {
                            //firebase realtime database diary 테이블에 입력
                            communityDAO.databaseReference?.child(key!!)?.setValue(community)?.addOnSuccessListener {
                                finish()
                            }?.addOnFailureListener {
                                Log.e(TAG, "community 테이블 입력 실패 $it")
                            }
                        }?.addOnFailureListener {
                            Log.e(TAG, "imageReference?.putFile Fail $it")
                        }
                    }else{
                        Toast.makeText(this,"사진을 선택해주세요",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}