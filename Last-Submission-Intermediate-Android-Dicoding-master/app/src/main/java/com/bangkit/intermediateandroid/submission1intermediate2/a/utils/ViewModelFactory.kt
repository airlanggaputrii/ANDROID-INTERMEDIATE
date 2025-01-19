package com.bangkit.intermediateandroid.submission1intermediate2.a.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.repository.StoryAppRepository
import com.bangkit.intermediateandroid.submission1intermediate2.a.di.Injection
import com.bangkit.intermediateandroid.submission1intermediate2.a.mainviewmodel.MainViewModel

class ViewModelFactory private constructor(private val storyAppRepository: StoryAppRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyAppRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}