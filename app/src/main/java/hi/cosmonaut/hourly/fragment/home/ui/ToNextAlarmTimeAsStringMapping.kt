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

package hi.cosmonaut.hourly.fragment.home.ui

import androidx.datastore.core.DataStore
import hi.cosmonaut.hourly.alarm.calendar.ToNextCalendarMapping
import hi.cosmonaut.hourly.proto.UserPreferences
import hi.cosmonaut.hourly.tool.mapping.SuspendMapping
import kotlinx.coroutines.CoroutineScope
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ToNextAlarmTimeAsStringMapping(
    private val dateFormat: DateFormat,
    private val mapping: SuspendMapping<DataStore<UserPreferences>, Calendar>,
) : SuspendMapping<DataStore<UserPreferences>, String> {

    constructor(
        scope: CoroutineScope
    ) : this(
        scope,
        "HH:mm"
    )

    constructor(
        scope: CoroutineScope,
        pattern: String,
    ) : this(
        scope,
        SimpleDateFormat(pattern, Locale.getDefault())
    )

    constructor(
        scope: CoroutineScope,
        dateFormat: DateFormat,
    ) : this(
        dateFormat,
        ToNextCalendarMapping(scope)
    )

    override suspend fun applyTo(input: DataStore<UserPreferences>): String {
        return dateFormat.format(
            mapping.applyTo(input).timeInMillis
        )
    }
}