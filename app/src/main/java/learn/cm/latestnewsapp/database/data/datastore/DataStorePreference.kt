package learn.cm.latestnewsapp.database.data.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceUtils {

    private var sharedPreference: SharedPreferences? = null

    private fun getPreferences(context: Context): SharedPreferences {
        if (sharedPreference==null){
            sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        }
        return sharedPreference!!
    }

    fun getEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }

    fun getString(key: String, defaultValue: String, context: Context): String? {
        return getPreferences(context).getString(key, defaultValue)
    }

    fun putString(key: String, value: String?, context: Context) {
        getEditor(context).putString(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean, context: Context): Boolean {
        return getPreferences(context).getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean, context: Context) {
        getEditor(context).putBoolean(key, value).apply()
    }

    fun clear(context: Context) {
        getEditor(context).clear().apply()
    }
}

