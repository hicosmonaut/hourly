package hi.cosmonaut.hourly.alarm.receiver

import android.content.Context
import android.content.Intent

class FiredAlarmReceiverIntent(
    intent: Intent
) : Intent(intent) {

    constructor(context: Context): this(
        context,
        context.packageName + ".action.TIME_ALARM"
    )

    constructor(context: Context, action: String): this(
        Intent(
            action,
            null,
            context,
            FiredAlarmBroadcastReceiver::class.java
        )
    )
}