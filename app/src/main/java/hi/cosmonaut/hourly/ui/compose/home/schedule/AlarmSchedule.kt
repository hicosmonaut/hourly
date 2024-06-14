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

package hi.cosmonaut.hourly.ui.compose.home.schedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import hi.cosmonaut.hourly.alarm.calendar.TimeArrayToNextCalendarMapping
import hi.cosmonaut.hourly.alarm.clock.NextAlarmClock
import hi.cosmonaut.hourly.intent.pending.ToFiredAlarmPendingIntentMapping
import hi.cosmonaut.hourly.tool.mapping.Mapping

class AlarmSchedule(
    private val context: Context,
    private val toFiredAlarmPendingIntentMapping: Mapping<Context, PendingIntent>,
    private val toNextCalendarMapping: TimeArrayToNextCalendarMapping,
    private val nextAlarmClock: NextAlarmClock,
) {

    constructor(
        context: Context,
    ) : this(
        context,
        ToFiredAlarmPendingIntentMapping(),
        TimeArrayToNextCalendarMapping(),
        NextAlarmClock(context)
    )

    fun cancelAll() {
        context.let {
            val alarmManager = it.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val firedAlarmPendingIntent = toFiredAlarmPendingIntentMapping.perform(it)

            alarmManager.cancel(
                firedAlarmPendingIntent
            )
        }
    }

    fun apply(startTime: Pair<Int, Int>, endTime: Pair<Int, Int>){
        nextAlarmClock.schedule(
            toNextCalendarMapping.perform(
                arrayOf(startTime, endTime)
            ).timeInMillis
        )
    }

}