package hi.cosmonaut.hourly.activity.main.listener

import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import hi.cosmonaut.hourly.permission.Permission

class GrantedOnClickListener(
    private val launcher: ActivityResultLauncher<Permission>,
    private val permission: Permission,
    private val cache: HashMap<String, View>,
    private val origin: View.OnClickListener,
) : View.OnClickListener, ActivityResultCallback<Boolean> {
    override fun onClick(v: View?) {
        if (permission.granted()) {
            origin.onClick(v)
        } else {
            v?.let { view ->
                cache["view"] = view
                launcher.launch(
                    permission
                )
            }
        }
    }

    override fun onActivityResult(result: Boolean) {
        if(result){
            cache["view"]?.let { v ->
                origin.onClick(v)
            }
        }
    }
}