package hi.cosmonaut.hourly.intent.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import hi.cosmonaut.hourly.tool.mapping.Mapping

class ToViewIntentMapping private constructor(
    private val mapping: Mapping<Int, Intent>,
) : Mapping<Int, Intent> {


    constructor(
        context: Context
    ) : this(
        Mapping { stringRes ->
            ViewIntent(
                Uri.parse(
                    context.getString(
                        stringRes
                    )
                )
            ).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        }
    )

    override fun perform(input: Int?): Intent = mapping.perform(input)
}