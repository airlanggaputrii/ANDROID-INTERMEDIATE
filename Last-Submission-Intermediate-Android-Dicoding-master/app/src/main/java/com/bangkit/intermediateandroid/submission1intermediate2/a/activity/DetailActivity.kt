package com.bangkit.intermediateandroid.submission1intermediate2.a.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bangkit.intermediateandroid.submission1intermediate2.R
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.local.UserPreference
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.ApiClient
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.ApiService
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.DetailStoryResponse
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.Story
import com.bangkit.intermediateandroid.submission1intermediate2.a.dateConverter
import com.bangkit.intermediateandroid.submission1intermediate2.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private val apiService = ApiClient.buildService(ApiService::class.java)
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Ngambil Id
        if (Build.VERSION.SDK_INT >= 33) {
            val id = intent.getStringExtra("id")
            Log.d("INI ID: ", id.toString())
            if(id!=null){
                renderContent(id)
            }

        } else {
            @Suppress("DEPRECATION")
            val id = intent.getStringExtra("id") as String
            Log.d("INI ID: ", id)
            renderContent(id)
        }

        //



    }


    private fun renderContent(id:String){

        // Initialize UserPreference with the application context
        UserPreference.init(applicationContext)

//        val service = ApiClient.buildService(ApiService::class.java)
        val token = UserPreference.getToken().toString()
        val bearertoken = "Bearer $token"
        apiService.getStoryDetail(id,bearertoken).enqueue(object: Callback<DetailStoryResponse>{
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    if(response.isSuccessful){
                        val data = response.body()?.story
                        Log.d("RESPON DETAIL", data.toString())
                        if (data != null) {
                            bindingData(data)
                        }
                    }
                    // ELSE HIASAN AJAH
                    else {
                        Toast.makeText(
                            this@DetailActivity,
                            "Erornya disini pak! : " + response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })
    }

    private fun bindingData(data: Story) {
        Glide.with(this)
            .load(data.photoUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = data.name
        binding.tvDetailDesc.text = data.description

        if (data.createdAt != null) {
            binding.tvDetailDate.text = dateConverter(data.createdAt)
        }
    }



}