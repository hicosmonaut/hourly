package hi.cosmonaut.hourly.intent.view

import android.content.Intent
import android.net.Uri

class ViewIntent(
    uri: Uri
) : Intent(
    ACTION_VIEW,
    uri
)