package kr.or.mrhi.letsgodaengdaeng.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kr.or.mrhi.letsgodaengdaeng.dataClass.Animal
import kr.or.mrhi.letsgodaengdaeng.dataClass.AnimalPhoto
import kr.or.mrhi.letsgodaengdaeng.dataClass.SeoulGil
import java.sql.SQLException

class DBHelper(val context: Context?, val name: String?, val version: Int) : SQLiteOpenHelper(context, name, null, version) {
    companion object {
        val PHOTO_NUM = 0
    }
    val TAG = this.javaClass.simpleName
    override fun onCreate(db: SQLiteDatabase?) {
        val query = """
            create table IF NOT EXISTS animal(
                num text,
                name text,
                breeds text,
                gender text,
                age text,
                weight text,
                intro text
            )
        """.trimIndent()
        val query2 = """
            create table IF NOT EXISTS animalPhoto(
                num text,
                photoNum text,
                photo text
            )
        """.trimIndent()
        val query3 = """
            create table IF NOT EXISTS seoulGil(
                name text,
                local text,
                distance text,
                time text,
                detailCourse text,
                courseLevel text,
                content text
            )
        """.trimIndent()
        db?.apply {
            execSQL(query)
            execSQL(query2)
            execSQL(query3)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, new: Int, old: Int) {
        val query = """
            drop table if exists animal
        """.trimIndent()
        val query2 = """
            drop table if exists animalPhoto
        """.trimIndent()
        val query3 = """
            drop table if exists seoulGil
        """.trimIndent()
        db?.apply {
            execSQL(query)
            execSQL(query2)
            execSQL(query3)
        }
        this.onCreate(db)
    }

    fun insertAnimal(animal: Animal): Boolean {
        var flag = false
        val query = """
            insert into animal values('${animal.num}','${animal.name}','${animal.breeds}',
            '${animal.gender}','${animal.age}','${animal.weight}kg','${animal.intro}')
        """.trimIndent()
        val db: SQLiteDatabase = writableDatabase
        try{
            db.execSQL(query)
            flag = true
        }catch (e: SQLException){
            Log.e(TAG, "insertAnimal 실패")
            flag = false
        } finally {
            db.close()
        }

        return flag
    }

    fun insertAnimalPhoto(animalPhoto: AnimalPhoto): Boolean {
        var flag = false
        val guery = """
            insert into animalPhoto values('${animalPhoto.num}','${animalPhoto.photoNum}','${animalPhoto.photo}')
        """.trimIndent()
        val db: SQLiteDatabase = writableDatabase
        try{
            db.execSQL(guery)
            flag = true
        }catch (e: SQLException){
            Log.d(TAG, "insertAnimalPhoto 실패")
            flag = false
        } finally {
            db.close()
        }
        return flag
    }

    fun selectAnimal(): MutableList<Animal>? {
        var animalList: MutableList<Animal>? = mutableListOf<Animal>()
        var cursor: Cursor? = null
        val query = """
            select * from animal
        """.trimIndent()
        val db = this.readableDatabase

        try {
            cursor = db.rawQuery(query, null)
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val num = cursor.getString(0)
                    val name = cursor.getString(1)
                    val breeds = cursor.getString(2)
                    val gender = cursor.getString(3)
                    val age = cursor.getString(4)
                    val weight = cursor.getString(5)
                    val intro = cursor.getString(6)
                    val animal = Animal(num, name, breeds, gender, age, weight, intro)
                    animalList?.add(animal)
                }
            } else {
                animalList = null
            }
        } catch (e: Exception) {
            Log.d(TAG, "selectAnimal $e")
            animalList = null
        } finally {
            cursor?.close()
            db.close()
        }
        return animalList
    }

    fun selectAllPhoto(num: String): MutableList<AnimalPhoto>? {
        var animalPhotoList: MutableList<AnimalPhoto>? = mutableListOf<AnimalPhoto>()
        var cursor: Cursor? = null
        val query = """
            select * from animalPhoto where num = '$num' and photoNum != '0'
        """.trimIndent()
        val db = this.readableDatabase

        try {
            cursor = db.rawQuery(query, null)
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val num = cursor.getString(0)
                    val photoNum = cursor.getString(1)
                    val photo = cursor.getString(2)
                    val animalPhotos = AnimalPhoto(num, photoNum, photo)
                    animalPhotoList?.add(animalPhotos)
                }
            } else {
                animalPhotoList = null
            }
        } catch (e: Exception) {
            Log.d(TAG, "selectAnimalPhoto $e")
            animalPhotoList = null
        } finally {
            cursor?.close()
            db.close()
        }
        return animalPhotoList
    }

    fun selectOnePhoto(num: String): String {
        var cursor: Cursor? = null
        val query = """
            select photo from animalPhoto where photoNum = '$PHOTO_NUM' and num = '$num'
        """.trimIndent()
        val db = this.readableDatabase
        var photo: String? = null
        try {
            cursor = db.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                photo = cursor.getString(0)
            }
        } catch (e: Exception) {
            Log.d(TAG, "selectPhoto $e")
        } finally {
            cursor?.close()
            db.close()
        }
        return photo!!
    }

    fun insertSeoulGil(seoulGil: SeoulGil): Boolean {
        var flag = false
        val query = """
            insert into seoulGil values('${seoulGil.name}','${seoulGil.local}','${seoulGil.distance}',
            '${seoulGil.time}','${seoulGil.detailCourse}','${seoulGil.courseLevel}','${seoulGil.content}')
        """.trimIndent()
        val db: SQLiteDatabase = writableDatabase
        try{
            db.execSQL(query)
            flag = true
        }catch (e: SQLException){
            Log.e(TAG, "insertAnimal 실패")
            flag = false
        } finally {
            db.close()
        }

        return flag
    }

    fun selectSeoulGil(): MutableList<SeoulGil>? {
        var seoulGilList: MutableList<SeoulGil>? = mutableListOf<SeoulGil>()
        var cursor: Cursor? = null
        val query = """
            select * from seoulGil
        """.trimIndent()
        val db = this.readableDatabase

        try {
            cursor = db.rawQuery(query, null)
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(0)
                    val local = cursor.getString(1)
                    val distance = cursor.getString(2)
                    val time = cursor.getString(3)
                    val detailCourse = cursor.getString(4)
                    val courseLevel = cursor.getString(5)
                    val content = cursor.getString(6)
                    val seoulGil = SeoulGil(name, local, distance, time, detailCourse, courseLevel,content)
                    seoulGilList?.add(seoulGil)
                }
            } else {
                seoulGilList = null
            }
        } catch (e: Exception) {
            Log.d(TAG, "selectSeoulGil $e")
            seoulGilList = null
        } finally {
            cursor?.close()
            db.close()
        }
        return seoulGilList
    }

}