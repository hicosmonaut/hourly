package hi.cosmonaut.hourly.activity.main.listener

import android.content.Context
import android.view.View
import hi.cosmonaut.hourly.alarm.calendar.ToNextCalendarMapping
import hi.cosmonaut.hourly.store.local.ToAppPreferencesMapping
import hi.cosmonaut.hourly.alarm.clock.NextAlarmClock

class OnApplyClickListener(
    private val toAppPreferencesMapping: ToAppPreferencesMapping,
    private val toNextCalendarMapping: ToNextCalendarMapping,
    private val nextAlarmClock: NextAlarmClock,
) : View.OnClickListener {

    constructor(
        context: Context
    ): this (
        ToAppPreferencesMapping(),
        ToNextCalendarMapping(),
        NextAlarmClock(
            context
        ),
    )

    override fun onClick(v: View?) {
        v?.let { view ->
            nextAlarmClock.schedule(
                toNextCalendarMapping.perform(
                    toAppPreferencesMapping.perform(
                        view.context
                    )
                ).timeInMillis
            )
        }
    }

}