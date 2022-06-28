package com.example.weatherapp2

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {

    lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            context.getString(R.string.shared_pref_tag),
            Context.MODE_PRIVATE
        )
    }
}
