package com.example.mycomposeskeleton.service

import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import android.content.Context
import android.content.SharedPreferences

class MySharedPref(context: Context) {


    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)


    var token: String?
    get() = sharedPreferences.getString(SHAREDPREF_SESSION_TOKEN, null)
    set(value) = sharedPreferences.edit().putString(SHAREDPREF_SESSION_TOKEN, value).apply()

    var userId: Long?
        get() = sharedPreferences.getLong(SHAREDPREF_SESSION_USER_ID, 0L)
        set(value) = sharedPreferences.edit().putLong(SHAREDPREF_SESSION_USER_ID, value ?: 0L).apply()

    fun clearSharedPref() {
        with(sharedPreferences) {
            edit()
                .remove(SHAREDPREF_SESSION_TOKEN)
                .remove(SHAREDPREF_SESSION_USER_ID)
                .apply()
        }
    }
}