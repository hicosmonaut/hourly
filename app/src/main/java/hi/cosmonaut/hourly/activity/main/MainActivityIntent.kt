package hi.cosmonaut.hourly.activity.main

import android.content.Context
import android.content.Intent

class MainActivityIntent(
    intent: Intent
) : Intent(intent) {

    constructor(context: Context): this(
        Intent(context, MainActivity::class.java).apply {
            this.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        }
    )

}