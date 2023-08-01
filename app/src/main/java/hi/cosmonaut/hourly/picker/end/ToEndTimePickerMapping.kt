package hi.cosmonaut.hourly.picker.end

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.store.local.Preferences
import hi.cosmonaut.hourly.tool.mapping.Mapping

class ToEndTimePickerMapping private constructor(
    private val cache: HashMap<String, MaterialTimePicker>,
    private val mapping: Mapping<Preferences, MaterialTimePicker>,
) : Mapping<Preferences, MaterialTimePicker> {

    constructor(
        fragmentManager: FragmentManager,
        tag: String,
    ) : this(
        hashMapOf(),
        Mapping { prefs ->
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(prefs.endTimeHour)
                .setMinute(prefs.endTimeMinute)
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTitleText(R.string.label_end_time)
                .build()

            picker.addOnPositiveButtonClickListener(
                OnEndTimePickerPositiveClickListener(
                    fragmentManager,
                    tag,
                    prefs
                )
            )

            picker

        }
    )

    override fun perform(input: Preferences?): MaterialTimePicker {
        if (!cache.containsKey("picker")) {
            cache["picker"] = mapping.perform(input)
        }
        return cache.getValue("picker")
    }
}