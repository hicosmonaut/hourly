package hi.cosmonaut.hourly.launcher.alarm

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import hi.cosmonaut.hourly.permission.Permission

class PermissionActivityResultContract private constructor(
    private val cache: HashMap<String, Permission>
): ActivityResultContract<Permission, Boolean>() {

    constructor(): this (
        hashMapOf()
    )

    override fun createIntent(context: Context, input: Permission): Intent {
        cache["permission"] = input
        return input.intent()
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        cache["permission"]?.granted() == true
}