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

package hi.cosmonaut.hourly.alarm.calendar

import android.util.Log
import androidx.datastore.core.DataStore
import hi.cosmonaut.hourly.proto.UserPreferences
import hi.cosmonaut.hourly.tool.mapping.SuspendMapping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class ToNextCalendarMapping(
    private val mapping: SuspendMapping<Flow<UserPreferences>, Calendar>,
) : SuspendMapping<DataStore<UserPreferences>, Calendar> {

    constructor(
        scope: CoroutineScope,
    ) : this(
        object : SuspendMapping<Flow<UserPreferences>, Calendar> {
            override suspend fun applyTo(input: Flow<UserPreferences>): Calendar {
                val prefs = input.stateIn(scope).value

                val startTimeHour = prefs.startHours
                val startTimeMinute = prefs.startMinutes
                val endTimeHour = prefs.endHours
                val endTimeMinute = prefs.endMinutes

                val current = Calendar.getInstance()

                Log.d(TAG, "current: ${current.time}")

                val start = Calendar.getInstance().apply {
                    this.set(Calendar.HOUR_OF_DAY, startTimeHour)
                    this.set(Calendar.MINUTE, startTimeMinute)
                    this.set(Calendar.SECOND, 0)
                    this.set(Calendar.MILLISECOND, 0)

                    if (current.after(this)) {
                        this.add(Calendar.DAY_OF_YEAR, 1)
                    }
                }

                Log.d(TAG, "start: ${start.time}")

                val end = Calendar.getInstance().apply {

                    this.set(Calendar.HOUR_OF_DAY, endTimeHour)
                    this.set(Calendar.MINUTE, endTimeMinute)
                    this.set(Calendar.SECOND, 0)
                    this.set(Calendar.MILLISECOND, 0)

                    if (endTimeHour < startTimeHour || (endTimeHour == startTimeHour && endTimeMinute <= startTimeMinute)) {
                        this.add(Calendar.DAY_OF_YEAR, 1)
                    }
                }

                Log.d(TAG, "end: ${end.time}")

                val new = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR_OF_DAY, 1)

                    this.set(Calendar.MINUTE, 0)
                    this.set(Calendar.SECOND, 0)
                    this.set(Calendar.MILLISECOND, 0)
                }

                Log.d(TAG, "new: ${new.time}")

                val resultMillis = when {
                    start.equals(end) -> {
                        Log.d(TAG, "resultMillis: 0")
                        new.timeInMillis
                    }

                    start.before(end) && (new.before(start) || new.equals(start) || new.after(start)) -> {
                        Log.d(TAG, "resultMillis: 1")
                        start.timeInMillis
                    }

                    end.before(start) && (current.equals(end) || current.after(end)) -> {
                        Log.d(TAG, "resultMillis: 2")
                        start.timeInMillis
                    }

                    end.before(start) && (new.equals(end) || new.after(end)) -> {
                        Log.d(TAG, "resultMillis: 3")
                        end.timeInMillis
                    }

                    end.before(start) && new.before(end) -> {
                        Log.d(TAG, "resultMillis: 4")
                        new.timeInMillis
                    }

                    else -> {
                        Log.d(TAG, "resultMillis: 5")
                        new.timeInMillis
                    }
                }

                val result = Calendar.getInstance().apply {
                    this.timeInMillis = resultMillis
                }

                Log.d(TAG, "result: ${result.time}")

                return result
            }
        }
    )

    override suspend fun applyTo(input: DataStore<UserPreferences>): Calendar = mapping.applyTo(input.data)

    companion object {
        private val TAG: String = ToNextCalendarMapping::class.java.simpleName
    }
}