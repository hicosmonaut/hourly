package hi.cosmonaut.hourly.store.local

import android.content.SharedPreferences

interface Preferences {
    var alarmEnabled: Boolean
    var startTimeHour: Int
    var startTimeMinute: Int
    var endTimeHour: Int
    var endTimeMinute: Int

    fun contains(key: String): Boolean
    fun remove(key: String)
    fun remove(vararg keys: String)
    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
}