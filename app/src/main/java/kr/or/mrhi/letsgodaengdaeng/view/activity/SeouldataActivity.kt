package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.api.*
import kr.or.mrhi.letsgodaengdaeng.api.SeoulGilWalkCourseApi.Companion.SeoulGil_API_KEY
import kr.or.mrhi.letsgodaengdaeng.api.SeoulGilWalkCourseApi.Companion.SeoulGil_LIMIT
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.ANIMAL_API_KEY
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.ANIMAL_LIMIT
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.PHOTO_API_KEY
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.PHOTO_LIMIT
import kr.or.mrhi.letsgodaengdaeng.dataClass.Animal
import kr.or.mrhi.letsgodaengdaeng.dataClass.AnimalPhoto
import kr.or.mrhi.letsgodaengdaeng.dataClass.Veterinary
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySeouldataBinding
import kr.or.mrhi.letsgodaengdaeng.retrofitData.Clinic.Library
import kr.or.mrhi.letsgodaengdaeng.retrofitData.SeoulGilWalkCourse.SeoulGil
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
        const val VERSION = 33
    }

    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySeouldataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** retrofit 객체 생성*/
        val retrofit = Retrofit.Builder()
            .baseUrl(TbAdpWaitAnimalApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofit2 = Retrofit.Builder()
            .baseUrl(SeoulGilWalkCourseApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val retrofit3 = Retrofit.Builder()
            .baseUrl(VeterinaryApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        /** retrofit 객체 인터페이스 전달*/
        val animalservice = retrofit.create(TbAdpWaitAnimal::class.java)
        val animalPhotoservice = retrofit.create(TbAdpWaitAnimalPhoto::class.java)
        val seoulGilservice = retrofit2.create(SeoulGilWalk::class.java)
        val veterinaryService = retrofit.create(VeterinaryClinic::class.java)


        val dbHelper = DBHelper(this, DB_NAME, VERSION)

        /** 인터페이스 함수를 오버라이딩해서 구현*/
        veterinaryService.getLibrarys(VeterinaryApi.API_KEY, VeterinaryApi.LIMIT)
            .enqueue(object : Callback<Library>{

                override fun onResponse(call: Call<Library>, response: Response<Library>) {
                    val data = response.body() // 모두가져온다

                    data.let {
                        for (loadData in it!!.LOCALDATA_020301.row){
                            if (loadData.TRDSTATEGBN == "01"){
                                val code = loadData.TRDSTATEGBN
                                val name = loadData.BPLCNM
                                val address = loadData.RDNWHLADDR
                                val phone = loadData.SITETEL
                                val veterinary = Veterinary(code, name, address, phone)
                                dbHelper.insertVeterinary(veterinary)
                            }
                        }
                    }
                } // end of onResponse

                override fun onFailure(call: Call<Library>, t: Throwable) {
                    Log.e(TAG, "veterinaryService.getLibrarys 정보 누락")
                }

            }) // end of veterinaryService.getLibrarys

        seoulGilservice.getSeoulGil(SeoulGil_API_KEY, SeoulGil_LIMIT)
            .enqueue(object : Callback<SeoulGil> {
                override fun onResponse(
                    call: Call<SeoulGil>,
                    response: Response<SeoulGil>
                ) {
                    val data = response.body()
                    data?.let {
                        for (loadData in it.SeoulGilWalkCourse.row) {
                            val name = loadData.COURSE_NAME
                            val local = loadData.AREA_GU
                            val distance = loadData.DISTANCE
                            val time = loadData.LEAD_TIME
                            val detailCourse = loadData.DETAIL_COURSE
                            val courseLevel = loadData.COURSE_LEVEL
                            val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(loadData.CONTENT, Html.FROM_HTML_MODE_LEGACY)
                                    .toString().replace("\n\n", "\n").replace("'", "")
                            } else {
                                Html.fromHtml(loadData.CONTENT).toString().replace("\n\n", "\n").replace("'", "")
                            }
                            val seoulGil= kr.or.mrhi.letsgodaengdaeng.dataClass.SeoulGil(name,local,distance,time,detailCourse,courseLevel,content)
                            dbHelper.insertSeoulGil(seoulGil)
                        } // end of for
                    } ?: let {
                        Log.e(TAG, "SeoulGilWalkCourse 정보 누락")
                    }
                } // end of onResponse

                override fun onFailure(call: Call<SeoulGil>, t: Throwable) {
                    Log.e(TAG, "seoulGilservice.getSeoulGil ${t.stackTraceToString()}")
                }
            }) // end of seoulGilservice.getSeoulGil

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