package hi.cosmonaut.hourly.store.local

import android.content.Context
import hi.cosmonaut.hourly.tool.mapping.Mapping

class ToAppPreferencesMapping private constructor(
    private val cache: HashMap<String, Preferences>,
    private val mapping: Mapping<Context, Preferences>,
) : Mapping<Context, Preferences> {

    constructor() : this(
        hashMapOf(),
        Mapping { c ->
            AppPreferences(c)
        }
    )

    override fun perform(input: Context?): Preferences {
        if (!cache.containsKey("prefs")) {
            cache["prefs"] = mapping.perform(input)
        }
        return cache.getValue("prefs")
    }

}