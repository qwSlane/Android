package com.plcoding.tabata.feature_drill.presentation.Preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.util.Log
import com.google.gson.Gson

class PreferencesSerializer {
    companion object {
        var preferences = Preferences(1, Language.ENGLISH, 12)
        var context: Context? = null
        var configuration: Configuration? = null

        fun saveContext(context: Context, config: Configuration) {
            this.context = context
            configuration = config
        }

        fun savePreferences(
            theme: Int = preferences.theme,
            fontSize: Int = preferences.fontSize,
            language: Language = preferences.language
        ) {
            Log.i("THEME", theme.toString())
            preferences = Preferences(theme, language,fontSize)
            val gson = Gson();
            val json = gson.toJson(preferences, Preferences::class.java)
            val pref = context!!.getSharedPreferences("settings", MODE_PRIVATE).edit()
            pref.putString("settings", json)
            pref.apply()
        }

        fun readPreferences() {
            val json = context!!.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("settings", "NULL")
            val gson = Gson()
            preferences = gson.fromJson(json, Preferences::class.java)
        }

    }
}
