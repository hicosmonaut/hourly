package hi.cosmonaut.hourly.intent.pending

import android.app.PendingIntent
import android.content.Context
import hi.cosmonaut.hourly.activity.main.MainActivityIntent
import hi.cosmonaut.hourly.tool.production.Production

class AlarmClockPendingIntentProduction(
    private val mapping: Production<PendingIntent>,
) : Production<PendingIntent> {

    constructor(
        context: Context,
    ) : this(
        context,
        100
    )

    constructor(
        context: Context,
        requestCode: Int,
    ) : this(
        Production {
            PendingIntent.getBroadcast(
                context,
                requestCode,
                MainActivityIntent(context),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    )

    override fun perform(): PendingIntent = mapping.perform()

}