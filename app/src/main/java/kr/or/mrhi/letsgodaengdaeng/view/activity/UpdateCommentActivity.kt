package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.CommunityVO
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityUpdateCommentBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.CommunityDAO
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UpdateCommentActivity : AppCompatActivity() {

    lateinit var binding :ActivityUpdateCommentBinding
    lateinit var category : String
    lateinit var filePath: String
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val communityDAO = CommunityDAO()
        val community = intent.getParcelableExtra<CommunityVO>("community")

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

        /** 수정할 글의 정보를 가져오기 */
        binding.edtContent.setText(community?.content)
        val imgRef = communityDAO.storage!!.reference.child("images/${community?.docID}.jpg")
        imgRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful){
                Glide.with(applicationContext)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }

        binding.btnBack.setOnClickListener {
            if(binding.edtContent.text.toString().equals("") && category.equals("카테고리 선택") && imageUri == null){
                finish()
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("수정하신 내용이 사라집니다. 작성화면에서 나가시겠습니까")
                    .setPositiveButton("네", DialogInterface.OnClickListener{ dialog, id->
                        finish()
                    })
                    .setNegativeButton("아니오", DialogInterface.OnClickListener{ dialog, id ->

                    })
                builder.show()
            }
        }
        /** 앨범에서 사진 가져오기 */
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

        /** 수정글 등록 및 업데이트 */
        binding.tvComplete.setOnClickListener {
            val communityDAO = CommunityDAO()
            val content = binding.edtContent.text.toString()
            val hashMap : HashMap<String,Any> = HashMap()
            hashMap["content"]= content
            hashMap["category"] =category
            //firebase storage 이미지를 업로드 경로명 셋팅한다.
            val imageReference = communityDAO.storage?.reference?.child("images/${community?.docID}.jpg")
            Log.d("pictureurl","${imageReference}")

            if(content.equals("")){
                Toast.makeText(this,"내용을 입력해주세요",Toast.LENGTH_SHORT).show()
            }else{
                if(category.equals("카테고리 선택")){
                    Toast.makeText(this,"카테고리를 선택해주세요",Toast.LENGTH_SHORT).show()
                }else{
                    if (imageUri != null){
                        imageReference?.putFile(imageUri!!)?.addOnSuccessListener {
                            communityDAO.updateCommunity(community?.docID!!,hashMap).addOnSuccessListener {
                            }?.addOnFailureListener {
                                Log.e("community", "community 테이블 입력 실패 $it")
                            }
                        }?.addOnFailureListener {
                            Log.e("community", "imageReference?.putFile Fail $it")
                            Toast.makeText(this,"스토리지저장실패!",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        communityDAO.updateCommunity(community?.docID!!,hashMap).addOnSuccessListener {
                        }?.addOnFailureListener {
                            Log.e("community", "community 테이블 입력 실패 $it")
                        }
                    }
                    finish()
                }

            }

        }

    }
}