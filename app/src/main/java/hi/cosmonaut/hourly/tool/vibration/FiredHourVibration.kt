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

package hi.cosmonaut.hourly.tool.vibration

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build

@SuppressLint("NewApi")
class FiredHourVibration(
    private val vibrators: Map<String, Vibration>
) : Vibration {

    constructor(context: Context) : this(
        mapOf(
            "obsolete" to ObsoleteVibration(context),
            "common" to CommonVibration(context)
        )

    )

    override fun launch(pattern: LongArray, repeatCount: Int) {
       val key = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
           "common"
       } else {
           "obsolete"
       }

        vibrators.getValue(key).launch(pattern, repeatCount)

    }

    override fun cancel() {
        val key = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            "common"
        } else {
            "obsolete"
        }

        vibrators.getValue(key).cancel()
    }
}