package com.bangkit.intermediateandroid.submission1intermediate2.a.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.local.UserPreference
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.ApiClient
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.ApiService
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.LoginResponse
import com.bangkit.intermediateandroid.submission1intermediate2.databinding.ActivityLoginBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiService = ApiClient.buildService(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.edLoginEmail.error = "Email is required"
                binding.edLoginEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edLoginPassword.error = "Password is required"
                binding.edLoginPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 8){
                return@setOnClickListener
            }

            loginUser(email, password)
        }


        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.progressBar.visibility = android.view.View.VISIBLE

        val requestBody = "{\"email\":\"$email\",\"password\":\"$password\"}"
            .toRequestBody("application/json".toMediaTypeOrNull())

        apiService.loginUser(requestBody).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                binding.progressBar.visibility = android.view.View.GONE

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse?.error == false) {
                        UserPreference.setToken(loginResponse.loginResult.token)
                        UserPreference.setUserId(loginResponse.loginResult.userId)
                        UserPreference.setName(loginResponse.loginResult.name)

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, loginResponse?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
