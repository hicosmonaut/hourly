package hi.cosmonaut.hourly.intent.pending

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import hi.cosmonaut.hourly.tool.mapping.Mapping
import hi.cosmonaut.hourly.tool.production.Production

class ToFiredAlarmClockInfoMapping(
    private val mapping: Mapping<Long, AlarmManager.AlarmClockInfo>,
) : Mapping<Long, AlarmManager.AlarmClockInfo> {

    constructor(
        context: Context,
    ) : this(
        AlarmClockPendingIntentProduction(context)
    )

    constructor(
        alarmClockPendingIntentProduction: Production<PendingIntent>,
    ) : this(
        Mapping { millis ->
            AlarmManager.AlarmClockInfo(
                millis,
                alarmClockPendingIntentProduction.perform()
            )
        }
    )

    override fun perform(input: Long?): AlarmManager.AlarmClockInfo = mapping.perform(input)


}