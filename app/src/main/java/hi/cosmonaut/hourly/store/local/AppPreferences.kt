/*
 * MIT License
 *
 * Copyright (c) 2023 Denis Kholodenin, hi.cosmonaut@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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