package hi.cosmonaut.hourly.permission

import android.content.Intent

interface Permission {
    fun intent(): Intent
    fun granted(): Boolean
}