/*
 * MIT License
 *
 * Copyright (c) 2023 Denis Kholodenin, hi.cosmonaut@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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