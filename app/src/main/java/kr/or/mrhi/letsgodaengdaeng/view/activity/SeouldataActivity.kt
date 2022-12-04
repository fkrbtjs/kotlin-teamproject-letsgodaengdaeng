package kr.or.mrhi.letsgodaengdaeng.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimal
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.ANIMAL_API_KEY
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalApi.Companion.ANIMAL_LIMIT
import kr.or.mrhi.letsgodaengdaeng.api.TbAdpWaitAnimalPhoto
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySeouldataBinding
import kr.or.mrhi.letsgodaengdaeng.retrofitData.tbAdpWaitAnimal.AnimalLibrary
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SeouldataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySeouldataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl(TbAdpWaitAnimalApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animalservice = retrofit.create(TbAdpWaitAnimal::class.java)
        val animalPhotoservice2 = retrofit.create(TbAdpWaitAnimalPhoto::class.java)


        animalservice.getLibrarys(ANIMAL_API_KEY, ANIMAL_LIMIT).enqueue(object: Callback<AnimalLibrary> {
            override fun onResponse(call: Call<AnimalLibrary>, response: Response<AnimalLibrary>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<AnimalLibrary>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}