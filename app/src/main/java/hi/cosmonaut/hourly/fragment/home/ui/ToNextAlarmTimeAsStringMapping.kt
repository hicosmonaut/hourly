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

import hi.cosmonaut.hourly.alarm.calendar.ToNextCalendarMapping
import hi.cosmonaut.hourly.store.local.Preferences
import hi.cosmonaut.hourly.tool.mapping.Mapping
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class ToNextAlarmTimeAsStringMapping(
    private val dateFormat: DateFormat,
    private val mapping: Mapping<Preferences, Calendar>,
) : Mapping<Preferences, String> {

    constructor() : this(
        "HH:mm"
    )

    constructor(
        pattern: String,
    ) : this(
        SimpleDateFormat(pattern)
    )

    constructor(
        dateFormat: DateFormat,
    ) : this(
        dateFormat,
        ToNextCalendarMapping()
    )

    override fun perform(input: Preferences?): String = dateFormat.format(
        mapping.perform(input).timeInMillis
    )
}