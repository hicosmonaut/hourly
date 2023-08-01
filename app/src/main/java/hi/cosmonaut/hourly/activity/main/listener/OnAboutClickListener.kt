package hi.cosmonaut.hourly.activity.main.listener

import android.content.Context
import android.content.Intent
import android.view.View
import hi.cosmonaut.hourly.intent.view.ToViewIntentMapping
import hi.cosmonaut.hourly.tool.mapping.Mapping

class OnAboutClickListener(
    private val linkStringResId: Int,
    private val toViewIntentMapping: Mapping<Int, Intent>
) : View.OnClickListener {

    constructor(
        context: Context,
        linkStringResId: Int,
    ): this(
        linkStringResId,
        ToViewIntentMapping(
            context
        )
    )

    override fun onClick(v: View?) {
        v?.context?.startActivity(
            toViewIntentMapping.perform(
                linkStringResId
            )
        )
    }
}