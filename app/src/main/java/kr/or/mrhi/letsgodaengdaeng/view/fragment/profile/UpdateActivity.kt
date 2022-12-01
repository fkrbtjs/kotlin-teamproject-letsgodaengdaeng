package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityUpdateBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.PuppyDAO

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    var imageUri: Uri? = null
    lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolUpdate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //휴대폰 내 앨범을 불러온다
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop() //사진을 자르지 않음
                    .into(binding.ivPicture)
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
        }// end of requestLauncher

        binding.ivPhotoUpdate.setOnClickListener{
            val intent =Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            requestLauncher.launch(intent)
        }


//        binding.btnUpdate.setOnClickListener{
//            val key = "key"
//            val puppyDAO = PuppyDAO()
//            val name = binding.edtPuppyName.text.toString()
//            val age = binding.edtPuppyAge.text.toString()
//            val breed = binding.edtPuppyBreed.text.toString()
//            val hashMap: HashMap<String, Any> = HashMap()
//            hashMap["puppyName"] = name
//            hashMap["puppyAge"] = name
//            hashMap["puppyBreed"] = name
//            puppyDAO.updatePuppy(key, hashMap).addOnSuccessListener {
//                Log.d("profile", "puppy update success")
//                val intent = Intent(this@UpdateActivity, InfoActivity::class.java)
//                startActivity(intent)
//                finish()
//            }.addOnFailureListener{
//                Log.d("profile", "puppy update fail")
//            }
//
//        }
//        binding.btnUpdate.isEnabled = true
    }

}