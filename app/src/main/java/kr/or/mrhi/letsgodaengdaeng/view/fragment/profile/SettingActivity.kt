package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySettingBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.LoginActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Actionbar -> Toolbar 변경
        setSupportActionBar(binding.toolSet)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userDAO = UserDAO()
        userDAO.selectUser(MainActivity.userCode!!)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userData in snapshot.children) {
                    val user: User? = snapshot.getValue(User::class.java)
                    binding.tvUserNickname.text = user?.nickname
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        /** 로그아웃을 눌렀을때 AlertDialog 띄우기*/
        binding.tvLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val dialog: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            dialog.setTitle("로그아웃")
            dialog.setMessage("로그아웃 하시겠습니까?")
            dialog.setPositiveButton("예", DialogInterface.OnClickListener{ dialog, which ->
                startActivity(intent)
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                finish()
            })
            dialog.setNegativeButton("아니오", DialogInterface.OnClickListener{ dialog, which ->
                dialog.dismiss()
            })
            dialog.show()
        }

        binding.btnWithdrawal.setOnClickListener {
            val intent = Intent(this, WithdrawalActivity::class.java)
            startActivity(intent)
        }

    }
    /**백버튼을 눌렀을떄 이동할 경로 지정*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}