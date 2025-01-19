package com.bangkit.intermediateandroid.submission1intermediate2.a.di

import android.content.Context
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.local.database.StoryDatabase
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.network.ApiConfig
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.repository.StoryAppRepository

object Injection {
    fun provideRepository(context: Context): StoryAppRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(database,apiService)
    }
}