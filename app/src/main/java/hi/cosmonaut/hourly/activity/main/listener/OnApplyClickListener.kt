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

package hi.cosmonaut.hourly.activity.main.listener

import android.content.Context
import android.view.View
import hi.cosmonaut.hourly.alarm.calendar.ToNextCalendarMapping
import hi.cosmonaut.hourly.store.local.ToAppPreferencesMapping
import hi.cosmonaut.hourly.alarm.clock.NextAlarmClock
import hi.cosmonaut.hourly.tool.extension.ContextExtension.userDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class OnApplyClickListener(
    private val scope: CoroutineScope,
    private val toNextCalendarMapping: ToNextCalendarMapping,
    private val nextAlarmClock: NextAlarmClock,
) : View.OnClickListener {

    constructor(
        context: Context,
        scope: CoroutineScope
    ): this (
        scope,
        ToNextCalendarMapping(scope),
        NextAlarmClock(
            context
        ),
    )

    override fun onClick(v: View?) {
        v?.let { view ->
            scope.launch {
                nextAlarmClock.schedule(
                    toNextCalendarMapping.applyTo(
                        view.context.userDataStore
                    ).timeInMillis
                )
            }
        }
    }

}