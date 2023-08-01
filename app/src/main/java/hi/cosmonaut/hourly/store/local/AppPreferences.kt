package hi.cosmonaut.hourly.store.local

import android.content.Context
import android.content.SharedPreferences

class AppPreferences private constructor(
    private val prefs: SharedPreferences,
) : Preferences {

    constructor(
        context: Context,
    ) : this(
        context.getSharedPreferences(context.packageName + ".prefs", Context.MODE_PRIVATE)
    )

    override fun contains(key: String): Boolean = prefs.contains(key)

    override fun remove(key: String) = prefs.edit().remove(key).apply()

    override fun remove(vararg keys: String) {
        val editor = prefs.edit()
        keys.forEach {
            editor.remove(it)
        }
        editor.apply()
    }

    override var alarmEnabled: Boolean
        get() = prefs.getBoolean("alarmEnabled", false)
        set(value) = prefs.edit().putBoolean("alarmEnabled", value).apply()
    override var startTimeHour: Int
        get() = prefs.getInt("startTimeHour", 9)
        set(value) = prefs.edit().putInt("startTimeHour", value).apply()
    override var startTimeMinute: Int
        get() = prefs.getInt("startTimeMinute", 0)
        set(value) = prefs.edit().putInt("startTimeMinute", value).apply()
    override var endTimeHour: Int
        get() = prefs.getInt("endTimeHour", 21)
        set(value) = prefs.edit().putInt("endTimeHour", value).apply()
    override var endTimeMinute: Int
        get() = prefs.getInt("endTimeMinute", 0)
        set(value) = prefs.edit().putInt("endTimeMinute", value).apply()

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}