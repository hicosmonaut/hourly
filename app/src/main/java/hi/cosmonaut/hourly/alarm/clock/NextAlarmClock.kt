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