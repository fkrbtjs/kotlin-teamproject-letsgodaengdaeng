package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.Animal
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityAnimalInfoBinding

class AnimalInfoActivity : AppCompatActivity() {
    lateinit var animalInfoFragment: AnimalInfoFragment
    lateinit var animalImageFragment: AnimalImageFragment
    var animal: Animal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnimalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animal = intent.getParcelableExtra("animal")

        binding.tvName.text = animal?.name

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        animalInfoFragment = AnimalInfoFragment()
        animalImageFragment = AnimalImageFragment()

        supportFragmentManager.beginTransaction().replace(R.id.animalFrameLayout, animalInfoFragment).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_animal_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_info -> { //정보
                supportFragmentManager.beginTransaction().replace(R.id.animalFrameLayout, animalInfoFragment).commit()
            }
            R.id.menu_image -> { //이미지
                supportFragmentManager.beginTransaction().replace(R.id.animalFrameLayout, animalImageFragment).commit()
            }
            android.R.id.home -> { //액티비티 종료
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}