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

package hi.cosmonaut.hourly.picker.start

import android.view.View
import androidx.datastore.core.DataStore
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import hi.cosmonaut.hourly.proto.UserPreferences
import hi.cosmonaut.hourly.tool.extension.ContextExtension.userDataStore
import hi.cosmonaut.hourly.tool.mapping.SuspendMapping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class OnStartTimeClockClickListener(
    private val fragmentManager: FragmentManager,
    private val tag: String,
    private val scope: CoroutineScope,
    private val toStartTimePickerMapping: SuspendMapping<DataStore<UserPreferences>, MaterialTimePicker>,
) : View.OnClickListener {

    constructor(
        fragmentManager: FragmentManager,
        tag: String,
        scope: CoroutineScope
    ) : this(
        fragmentManager,
        tag,
        scope,
        ToStartTimePickerMapping(
            fragmentManager,
            tag,
            scope
        ),
    )

    override fun onClick(v: View) {
        scope.launch {
            val dataStore = v.context.userDataStore
            val picker = toStartTimePickerMapping.applyTo(dataStore)
            picker.show(fragmentManager, tag)
        }
    }

}