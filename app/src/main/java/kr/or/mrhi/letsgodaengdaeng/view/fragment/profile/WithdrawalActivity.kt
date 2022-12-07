package kr.or.mrhi.letsgodaengdaeng.view.fragment.profile

import android.R
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.letsgodaengdaeng.dataClass.User
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityWithdrawalBinding
import kr.or.mrhi.letsgodaengdaeng.databinding.DialogLayoutBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.UserDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.LoginActivity
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity

class WithdrawalActivity : AppCompatActivity() {
    lateinit var binding: ActivityWithdrawalBinding
    lateinit var bindingDialog: DialogLayoutBinding
    var dialog: AlertDialog.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        bindingDialog = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**Actionbar -> Toolbar 변경*/
        setSupportActionBar(binding.toolWithdrawal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.cbConsent.setOnClickListener {
            if (binding.cbConsent.isChecked) {
                binding.btnWithdrawal.visibility = View.VISIBLE
                Log.d("profileactivity", "나와라")
            } else {
                binding.btnWithdrawal.visibility = View.INVISIBLE
                Log.d("profileactivity", "숨어라")
            }
        }
        /**버튼이 눌려을때 본인확인을 위한 다이얼로그 화면을 띄운다*/
        binding.btnWithdrawal.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            dialog = AlertDialog.Builder(
                this,
                R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            val userDAO = UserDAO()
            dialog?.setTitle("회원탈퇴")
            dialog?.setMessage("패스워드를 입력해주세요")
            if (bindingDialog.root.parent != null) {
                (bindingDialog.root.parent as ViewGroup).removeView(bindingDialog.root)
            }
            dialog?.setView(bindingDialog.root)
            dialog?.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                userDAO.selectUser(MainActivity.userCode!!)
                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user: User? = snapshot.getValue(User::class.java)
                            /**현재 로그인된 유저의 패스워드가 일치하면 회원탈퇴*/
                            if (user?.password == bindingDialog.edtDialog.text.toString()) {
                                Log.e("letsgodaengdaeng", "delete")
                                deleteData()
                                startActivity(intent)
                                Toast.makeText(this@WithdrawalActivity,"회원탈퇴 되었습니다. 이용해주셔서 감사합니다.",Toast.LENGTH_SHORT).show()

                            } else {
                                Log.e("letsgodaengdaeng", "delete no")
                                Toast.makeText(
                                    this@WithdrawalActivity,
                                    "패스워드가 일치하지 않습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("letsgodaengdaeng", "${error.toString()}")
                        }
                    })
                finish()
            })
            dialog?.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
//            dialog?.setCancelable(true)
//            dialog?.create()
            dialog?.show()
        }
    }

    /**유저 삭제를 위한 함수*/
    private fun deleteData() {
        val userDAO = UserDAO()
        userDAO.deleteUser(MainActivity.userCode!!).addOnSuccessListener {
            Toast.makeText(applicationContext, "User delete success", Toast.LENGTH_SHORT).show()
            Log.d("letsgodaengdaeng", "User delete success")
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "User delete no", Toast.LENGTH_SHORT).show()
            Log.d("letsgodaengdaeng", "User delete no")
        }
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