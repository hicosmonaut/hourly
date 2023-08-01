package hi.cosmonaut.hourly.fragment.home.ui

import hi.cosmonaut.hourly.alarm.calendar.ToNextCalendarMapping
import hi.cosmonaut.hourly.store.local.Preferences
import hi.cosmonaut.hourly.tool.mapping.Mapping
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class ToNextAlarmTimeAsStringMapping(
    private val dateFormat: DateFormat,
    private val mapping: Mapping<Preferences, Calendar>,
) : Mapping<Preferences, String> {

    constructor() : this(
        "HH:mm"
    )

    constructor(
        pattern: String,
    ) : this(
        SimpleDateFormat(pattern)
    )

    constructor(
        dateFormat: DateFormat,
    ) : this(
        dateFormat,
        ToNextCalendarMapping()
    )

    override fun perform(input: Preferences?): String = dateFormat.format(
        mapping.perform(input).timeInMillis
    )
}