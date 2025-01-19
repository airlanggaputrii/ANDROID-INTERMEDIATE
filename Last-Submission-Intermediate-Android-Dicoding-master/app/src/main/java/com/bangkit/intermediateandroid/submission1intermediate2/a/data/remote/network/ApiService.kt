package com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.network

import com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.response.StoryResponse
import retrofit2.http.*

interface ApiService {

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse



}