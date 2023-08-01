package hi.cosmonaut.hourly.fragment.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import hi.cosmonaut.hourly.store.local.AppPreferences
import hi.cosmonaut.hourly.store.local.Preferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeViewModel(
    app: Application,
    private val prefs: Preferences,
) : AndroidViewModel(app) {

    constructor(
        application: Application,
    ) : this(
        application,
        AppPreferences(application),
    )

    val timeFlow: Flow<HashMap<String, Int>> = flow {
        val times = hashMapOf<String, Int>()
        while (true) {
            times["endTimeHour"] = prefs.endTimeHour
            times["endTimeMinute"] = prefs.endTimeMinute
            times["startTimeHour"] = prefs.startTimeHour
            times["startTimeMinute"] = prefs.startTimeMinute
            emit(times)
            delay(1000L)
        }
    }

    companion object {
        private val TAG: String = HomeViewModel::class.java.simpleName
    }

}