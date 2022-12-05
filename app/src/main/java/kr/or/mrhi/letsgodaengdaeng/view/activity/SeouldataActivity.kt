package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimal
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.ANIMAL_API_KEY
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.ANIMAL_LIMIT
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.PHOTO_API_KEY
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.PHOTO_LIMIT
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalPhoto
import kr.or.mrhi.letsgodaengdaeng.dataClass.Animal
import kr.or.mrhi.letsgodaengdaeng.dataClass.AnimalPhoto
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySeouldataBinding
import kr.or.mrhi.letsgodaengdaeng.retrofitData.tbAdpWaitAnimal.AnimalLibrary
import kr.or.mrhi.letsgodaengdaeng.retrofitData.tbAdpWaitAnimalPhoto.AnimalPhotoLibrary
import kr.or.mrhi.letsgodaengdaeng.sqlite.DBHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SeouldataActivity : AppCompatActivity() {
    companion object {
        const val DB_NAME = "testDB"
        const val VERSION = 21
<<<<<<< HEAD
    } //version 20
=======
    }// 커밋전에 버전 20으로 만들기
>>>>>>> 979bad2d995ec11e179fb81571567496491ec756

    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySeouldataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl(TbAdpWaitAnimalApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animalservice = retrofit.create(TbAdpWaitAnimal::class.java)
        val animalPhotoservice = retrofit.create(TbAdpWaitAnimalPhoto::class.java)

        val dbHelper = DBHelper(this, DB_NAME, VERSION)

        animalservice.getLibrarys(ANIMAL_API_KEY, ANIMAL_LIMIT)
            .enqueue(object : Callback<AnimalLibrary> {
                override fun onResponse(
                    call: Call<AnimalLibrary>,
                    response: Response<AnimalLibrary>
                ) {
                    val data = response.body()
                    data?.let {
                        for (loadData in it.TbAdpWaitAnimalView.row) {
                            val num = loadData.ANIMAL_NO
                            val name = loadData.NM
                            val breeds = loadData.BREEDS
                            val gender = if (loadData.SEXDSTN == "W") {
                                "여아"
                            } else {
                                "남아"
                            }
                            val age = loadData.AGE
                            val weight = loadData.BDWGH
                            val intro = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(loadData.INTRCN_CN, Html.FROM_HTML_MODE_LEGACY)
                                    .toString().replace("\n\n", "\n").replace("'", "")
                            } else {
                                Html.fromHtml(loadData.INTRCN_CN).toString().replace("\n\n", "\n").replace("'", "")
                            }
                            val animal = Animal(num, name, breeds, gender, age, weight, intro)
                            dbHelper.insertAnimal(animal)
                        } // end of for
                    } ?: let {
                        Log.e(TAG, "TbAdpWaitAnimalView 정보 누락")
                    }
                } // end of onResponse

                override fun onFailure(call: Call<AnimalLibrary>, t: Throwable) {
                    Log.e(TAG, "animalservice.getLibrarys ${t.stackTraceToString()}")
                }
            }) // end of animalservice.getLibrarys

        animalPhotoservice.getLibrarys(PHOTO_API_KEY, PHOTO_LIMIT).enqueue(object: Callback<AnimalPhotoLibrary> {
            override fun onResponse(call: Call<AnimalPhotoLibrary>, response: Response<AnimalPhotoLibrary>) {
                val data = response.body()
                data?.let {
                    var number = 0
                    var num: String? = null
                    var photoNum: String? = null
                    var photo: String? = null
                    for (loadData in it.TbAdpWaitAnimalPhotoView.row) {
                        /** 공공데이터 사진이 동물당 9장이고 사진번호가 각기 달라 0~8 으로 저장하기 위함 */
                        if (num != loadData.ANIMAL_NO) {
                            number = 0
                            num = loadData.ANIMAL_NO
                            photoNum = number.toString()
                            photo = loadData.PHOTO_URL
                        } else {
                            number++
                            num = loadData.ANIMAL_NO
                            photoNum = number.toString()
                            photo = loadData.PHOTO_URL
                        }
                        val animalPhoto = AnimalPhoto(num.toString(), photoNum, photo)
                        dbHelper.insertAnimalPhoto(animalPhoto)
                        Log.e(TAG, "${dbHelper.insertAnimalPhoto(animalPhoto)}")
                        Log.e(TAG, "$num $photoNum $photo")
                    } // end of for
                    val intent = Intent(this@SeouldataActivity, LoginActivity::class.java)
                    startActivity(intent)
                } ?: let {
                    Log.e(TAG, "TbAdpWaitAnimalPhotoView 정보 누락")
                }
            } // end of onResponse
            override fun onFailure(call: Call<AnimalPhotoLibrary>, t: Throwable) {
                Log.e(TAG, "animalPhotoservice.getLibrarys ${t.stackTraceToString()}")
            }
        }) // end of animalPhotoservice.getLibrarys
    } // end of onCreate
}