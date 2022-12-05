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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.Puppy
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityUpdateBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.PuppyDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    var imageUri: Uri? = null
    lateinit var filePath: String
    val puppyDAO = PuppyDAO()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolUpdate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val puppyImg = puppyDAO.storage!!.reference.child("puppyImage/${MainActivity.userCode}.jpg")
        puppyImg.downloadUrl.addOnCompleteListener{
            if(it.isSuccessful){
                Glide.with(applicationContext)
                    .load(it.result)
                    .into(binding.ivPicture)
            }
        }
        puppyDAO.selectPuppy(MainActivity.userCode!!)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userData in snapshot.children){
                    val puppy: Puppy? = snapshot.getValue(Puppy::class.java)
                    binding.edtPuppyName.setText("${puppy?.name}")
                    binding.edtPuppyBreed.setText("${puppy?.breed}")
                    binding.edtTendency.setText("${puppy?.tendency}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

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

        binding.ivPhotoUpdate.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        //강아지 정보 업데이트
        binding.btnUpdate.setOnClickListener{

            val name = binding.edtPuppyName.text.toString()
            val breed = binding.edtPuppyBreed.text.toString()
            val tendency = binding.edtTendency.text.toString()
            var gender: String? = null
            gender = if (binding.btnMan.isChecked) {
                "남아"
            } else {
                "여아"
            }

            var size: String? = null
            size = if(binding.rbtnSizeLarge.isChecked){
                "대형견"
            }else if(binding.rbtnSizeMedium.isChecked){
                "중형견"
            }else{
                "소형견"
            }

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["name"] = name
            hashMap["gender"] = breed
            hashMap["tendency"] = tendency
            hashMap["gender"] = gender
            hashMap["size"] = size
            puppyDAO.updatePuppy(MainActivity.userCode!!, hashMap).addOnSuccessListener {
                Log.d("letsgodaengdaeng", "puppy update success")
                finish()
            }.addOnFailureListener{
                Log.d("letsgodaengdaeng", "puppy update fail")
            }
            val updatePuppyImg = puppyDAO.storage?.reference?.child("puppyImage/${MainActivity.userCode}.jpg")
            updatePuppyImg?.putFile(imageUri!!)?.addOnSuccessListener {
                Log.d("letsgodaengdaeng", "Success")
            }?.addOnFailureListener{
                Log.d("letsgodaengdaeng", "Fail")
            }
        }
        binding.btnUpdate.isEnabled = true
    }

    //뒤로가기버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
