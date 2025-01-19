package com.bangkit.intermediateandroid.submission1intermediate2.a.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.StoryRemoteMediator
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.local.Preference
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.local.database.StoryDatabase
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.network.ApiService
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.response.ListStoryItem


class StoryAppRepository(private val database : StoryDatabase, private val apiService: ApiService) {


    fun setPreference(token: String, context: Context) {
        val settingPreference = Preference(context)
        settingPreference.setUser(token)
    }
    fun getPreference(context: Context): String? {
        val settingPreference = Preference(context)
        return settingPreference.getUser()
    }

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator =  StoryRemoteMediator(token, database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }
}