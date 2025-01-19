package com.bangkit.intermediateandroid.submission1intermediate2.a.mainviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.response.ListStoryItem
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.repository.StoryAppRepository


class MainViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {

    private val token = MutableLiveData<String?>()

    fun setPreference(token: String, context: Context) = storyAppRepository.setPreference(token,context)
    fun getPreference(context: Context): LiveData<String?> {
        val tokenData = storyAppRepository.getPreference(context)
        token.value = tokenData
        return token
    }

    fun story(token: String): LiveData<PagingData<ListStoryItem>> =
        storyAppRepository.getStory(token).cachedIn(viewModelScope)
}