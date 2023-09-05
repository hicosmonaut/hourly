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

package hi.cosmonaut.hourly.picker.end

import androidx.datastore.core.DataStore
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.proto.UserPreferences
import hi.cosmonaut.hourly.tool.mapping.SuspendMapping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ToEndTimePickerMapping private constructor(
    private val cache: HashMap<String, MaterialTimePicker>,
    private val mapping: SuspendMapping<Flow<UserPreferences>, MaterialTimePicker>,
) : SuspendMapping<DataStore<UserPreferences>, MaterialTimePicker> {

    constructor(
        fragmentManager: FragmentManager,
        tag: String,
        scope: CoroutineScope,
    ) : this(
        hashMapOf(),
        object : SuspendMapping<Flow<UserPreferences>, MaterialTimePicker> {
            override suspend fun applyTo(input: Flow<UserPreferences>): MaterialTimePicker {
                return input.map {
                    val picker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(it.endHours)
                        .setMinute(it.endMinutes)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                        .setTitleText(R.string.label_start_time)
                        .build()

                    picker.addOnPositiveButtonClickListener(
                        OnEndTimePickerPositiveClickListener(
                            fragmentManager,
                            tag,
                            scope
                        )
                    )

                    picker
                }
                    .stateIn(scope)
                    .value
            }
        }
    )


    override suspend fun applyTo(input: DataStore<UserPreferences>): MaterialTimePicker {
        if (!cache.containsKey("picker")) {
            cache["picker"] = mapping.applyTo(input.data)
        }
        return cache.getValue("picker")
    }
}