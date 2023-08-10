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

package hi.cosmonaut.hourly.alarm.clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import hi.cosmonaut.hourly.intent.pending.FiredAlarmPendingIntentProduction
import hi.cosmonaut.hourly.intent.pending.ToFiredAlarmClockInfoMapping
import hi.cosmonaut.hourly.tool.mapping.Mapping
import hi.cosmonaut.hourly.tool.production.Production

class NextAlarmClock(
    private val context: Context,
    private val toFiredAlarmClockInfoMapping: Mapping<Long, AlarmManager.AlarmClockInfo>,
    private val firedAlarmPendingIntentProduction: Production<PendingIntent>,
) : AlarmClock {

    constructor(
        context: Context,
    ) : this(
        context,
        ToFiredAlarmClockInfoMapping(context),
        FiredAlarmPendingIntentProduction(context)
    )

    override fun schedule(millis: Long) {
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).run {
            setAlarmClock(
                toFiredAlarmClockInfoMapping.perform(millis),
                firedAlarmPendingIntentProduction.perform()
            )
        }
    }
}