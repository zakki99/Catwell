package Dokter.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.collaboracrew.catwell.DoctorMainActivity
import com.collaboracrew.catwell.R
import com.collaboracrew.catwell.api.ApiRetrofit
import com.collaboracrew.catwell.model.LoginModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login_Dokter : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var btLogin: Button
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_dokter)

        val txtviewsignup: TextView = findViewById(R.id.txt_signup)
        txtviewsignup.setOnClickListener {
            startActivity(Intent(this@Login_Dokter, SignUp_Dokter::class.java))
            finish()
        }

        val txtForgotPass: TextView = findViewById(R.id.txt_forgot)
        txtForgotPass.setOnClickListener {
            startActivity(Intent(this@Login_Dokter, LupaPasswordDokter::class.java))
            finish()
        }

        setupView()
        setupListener()
    }

    private fun setupView() {
        btLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.emailDc)
        etPass = findViewById(R.id.passwdDc)
    }

    private fun setupListener() {
        btLogin.setOnClickListener {
            val emailUser = etEmail.text.toString()
            val passUser = etPass.text.toString()

            if (emailUser.isNotEmpty() && passUser.isNotEmpty()) {
                Log.e("RegisterActivity", emailUser)
                api.login(emailUser, passUser)
                    .enqueue(object : Callback<LoginModel> {
                        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                            Log.e("LoginActivity", t.toString())
                            Toast.makeText(
                                applicationContext,
                                "Login failed: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<LoginModel>,
                            response: Response<LoginModel>
                        ) {
                            if (response.isSuccessful) {
                                val loginResponse = response.body()

                                if (loginResponse?.success == true) {
                                    startActivity(
                                        Intent(
                                            this@Login_Dokter,
                                            DoctorMainActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Invalid email or password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                val errorResponse = response.errorBody()?.string()
                                Toast.makeText(
                                    applicationContext,
                                    "Login failed: $errorResponse",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            } else {
                Toast.makeText(
                    applicationContext,
                    "Data Tidak Boleh Kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
