package hi.cosmonaut.hourly.picker.start

import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import hi.cosmonaut.hourly.store.local.Preferences

class OnStartTimePickerPositiveClickListener(
    private val fragmentManager: FragmentManager,
    private val tag: String,
    private val prefs: Preferences,
) : View.OnClickListener {
    override fun onClick(v: View?) {
        if (!fragmentManager.isDestroyed) {
            val unsafePicker = (fragmentManager.findFragmentByTag(tag)) as? MaterialTimePicker
            unsafePicker?.let { picker ->
                prefs.startTimeHour = picker.hour
                prefs.startTimeMinute = picker.minute
            }
        }
    }

}