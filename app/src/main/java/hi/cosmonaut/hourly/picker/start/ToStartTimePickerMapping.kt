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

package hi.cosmonaut.hourly.picker.start

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.store.local.Preferences
import hi.cosmonaut.hourly.tool.mapping.Mapping

class ToStartTimePickerMapping private constructor(
    private val cache: HashMap<String, MaterialTimePicker>,
    private val mapping: Mapping<Preferences, MaterialTimePicker>,
) : Mapping<Preferences, MaterialTimePicker> {

    constructor(
        fragmentManager: FragmentManager,
        tag: String,
    ) : this(
        hashMapOf(),
        Mapping { prefs ->
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(prefs.startTimeHour)
                .setMinute(prefs.startTimeMinute)
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTitleText(R.string.label_start_time)
                .build()

            picker.addOnPositiveButtonClickListener(
                OnStartTimePickerPositiveClickListener(
                    fragmentManager,
                    tag,
                    prefs
                )
            )

            picker
        }
    )

    override fun perform(input: Preferences?): MaterialTimePicker {
        if (!cache.containsKey("picker")) {
            cache["picker"] = mapping.perform(input)
        }
        return cache.getValue("picker")
    }
}