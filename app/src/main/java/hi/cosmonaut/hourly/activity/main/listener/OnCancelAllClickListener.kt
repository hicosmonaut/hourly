package hi.cosmonaut.hourly.activity.main.listener

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.view.View
import hi.cosmonaut.hourly.intent.pending.ToFiredAlarmPendingIntentMapping
import hi.cosmonaut.hourly.store.local.Preferences
import hi.cosmonaut.hourly.store.local.ToAppPreferencesMapping
import hi.cosmonaut.hourly.tool.mapping.Mapping

class OnCancelAllClickListener(
    private val toAppPreferencesMapping: Mapping<Context, Preferences>,
    private val toFiredAlarmPendingIntentMapping: Mapping<Context, PendingIntent>
) : View.OnClickListener {

    constructor(): this(
        ToAppPreferencesMapping(),
        ToFiredAlarmPendingIntentMapping()
    )

    override fun onClick(v: View?) {
        v?.context?.let {
            val alarmManager = it.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val prefs = toAppPreferencesMapping.perform(it)
            val firedAlarmPendingIntent = toFiredAlarmPendingIntentMapping.perform(it)
            
            prefs.alarmEnabled = false
            alarmManager.cancel(
                firedAlarmPendingIntent
            )
        }
    }
}