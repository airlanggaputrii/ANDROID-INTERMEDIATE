package com.bangkit.intermediateandroid.submission1intermediate2.a.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun registerUser(@Body user: User): Call<Void>

    @POST("login")
    fun loginUser(@Body requestBody: RequestBody): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Call<UploadResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getStoryDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<DetailStoryResponse>

    @GET("stories")
    fun getAllStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): Call<StoryResponse>

    @GET("stories")
    fun getAllStoriesWithPaging3(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): Call<StoryResponse>
}
