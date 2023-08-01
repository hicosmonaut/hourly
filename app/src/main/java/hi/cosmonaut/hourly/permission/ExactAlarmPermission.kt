package hi.cosmonaut.hourly.permission

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

class ExactAlarmPermission(
    private val alarmManager: AlarmManager,
    private val intent: Intent
) : Permission {

    constructor(
        context: Context,
    ) : this(
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
        Intent()
    )

    override fun intent(): Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        intent.apply {
            this.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
        }
    } else {
        intent
    }

    override fun granted(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }

}