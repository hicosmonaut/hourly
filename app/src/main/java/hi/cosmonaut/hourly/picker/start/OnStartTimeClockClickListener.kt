package hi.cosmonaut.hourly.picker.start

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import hi.cosmonaut.hourly.store.local.AppPreferences
import hi.cosmonaut.hourly.store.local.Preferences
import hi.cosmonaut.hourly.tool.mapping.Mapping

class OnStartTimeClockClickListener(
    private val fragmentManager: FragmentManager,
    private val tag: String,
    private val prefs: Preferences,
    private val toStartTimePickerMapping: Mapping<Preferences, MaterialTimePicker>,
) : View.OnClickListener {

    constructor(
        context: Context,
        fragmentManager: FragmentManager,
        tag: String,
    ) : this(
        fragmentManager,
        tag,
        AppPreferences(context),
    )

    constructor(
        fragmentManager: FragmentManager,
        tag: String,
        prefs: Preferences,
    ) : this(
        fragmentManager,
        tag,
        prefs,
        ToStartTimePickerMapping(
            fragmentManager,
            tag
        )
    )

    override fun onClick(v: View?) {
        val picker = toStartTimePickerMapping.perform(prefs)
        picker.show(fragmentManager, tag)
    }

}