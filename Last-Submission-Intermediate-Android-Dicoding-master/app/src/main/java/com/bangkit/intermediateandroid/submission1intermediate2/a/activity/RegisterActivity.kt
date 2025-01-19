package com.bangkit.intermediateandroid.submission1intermediate2.a.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.ApiClient
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.ApiService
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.User
import com.bangkit.intermediateandroid.submission1intermediate2.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val apiService: ApiService by lazy { ApiClient.buildService(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // peringatan pw minimal 8 karakter
//        binding.edRegisterPassword.addTextChangedListener {
//            val password = it.toString().trim()
//            if (password.length < 8) {
//                binding.edRegisterPassword.error = "Password harus terdiri dari minimal 8 karakter"
//            } else {
//                binding.edRegisterPassword.error = null
//            }
//        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8){
                return@setOnClickListener
            }

            val user = User(name, email, password)
            binding.progressBar.visibility = android.view.View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch{
                apiService.registerUser(user).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        binding.progressBar.visibility = android.view.View.GONE
                        if (response.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "Registration success", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        binding.progressBar.visibility = android.view.View.GONE
                        Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }
    }
}
