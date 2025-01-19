package com.bangkit.intermediateandroid.submission1intermediate2.a.data.local

import android.content.Context
import android.content.SharedPreferences

object UserPreference {

    private const val PREFS_NAME = "user_pref"
    private const val TOKEN = "token"
    private const val USER_ID = "user_id"
    private const val NAME = "name"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setToken(value: String) {
        sharedPreferences.edit().putString(TOKEN, value).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    fun setUserId(value: String) {
        sharedPreferences.edit().putString(USER_ID, value).apply()
    }

//    fun getUserId(): String? {
//        return sharedPreferences.getString(USER_ID, null)
//    }

    fun setName(value: String) {
        sharedPreferences.edit().putString(NAME, value).apply()
    }

//    fun getName(): String? {
//        return sharedPreferences.getString(NAME, null)
//    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

}
