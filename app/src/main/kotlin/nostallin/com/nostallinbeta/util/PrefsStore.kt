package nostallin.com.nostallinbeta.util

import android.content.Context
import android.content.SharedPreferences
import nostallin.com.nostallinbeta.app.NoStallinApplication

class PrefsStore {

    private val sharedPrefs: SharedPreferences = NoStallinApplication.get().getSharedPreferences("nostallin", Context.MODE_PRIVATE)

    fun saveId(name: String, id: String) {
        with(sharedPrefs.edit()) {
            putString(name, id)
            apply()
        }
    }
}