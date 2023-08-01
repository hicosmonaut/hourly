package hi.cosmonaut.hourly.intent.alarm

import android.content.Intent
import android.provider.Settings

class ExactAlarmPermissionIntent : Intent(
    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
)