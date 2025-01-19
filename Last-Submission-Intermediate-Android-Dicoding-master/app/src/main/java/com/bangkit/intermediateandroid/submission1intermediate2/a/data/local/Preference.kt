package com.bangkit.intermediateandroid.submission1intermediate2.a.data.local

import android.content.Context

internal class Preference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(token: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }
    fun getUser(): String? = preferences.getString(TOKEN, "")

}