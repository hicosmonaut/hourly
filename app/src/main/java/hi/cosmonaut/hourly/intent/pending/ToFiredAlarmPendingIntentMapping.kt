package hi.cosmonaut.hourly.intent.pending

import android.app.PendingIntent
import android.content.Context
import hi.cosmonaut.hourly.alarm.receiver.FiredAlarmReceiverIntent
import hi.cosmonaut.hourly.tool.mapping.Mapping

class ToFiredAlarmPendingIntentMapping(
    private val mapping: Mapping<Context, PendingIntent>,
) : Mapping<Context, PendingIntent> {

    constructor(
    ) : this(
        200
    )

    constructor(
        requestCode: Int,
    ) : this(
        Mapping { c ->
            PendingIntent.getBroadcast(
                c,
                requestCode,
                FiredAlarmReceiverIntent(c),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    )

    override fun perform(input: Context?): PendingIntent = mapping.perform(input)

}