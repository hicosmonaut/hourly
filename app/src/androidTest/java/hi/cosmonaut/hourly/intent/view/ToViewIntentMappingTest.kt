/*
 * MIT License
 *
 * Copyright (c) 2022 Denis Kholodenin, hi.cosmonaut@gmail.com
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

package hi.cosmonaut.hourly.intent.view

import android.net.Uri
import androidx.test.InstrumentationRegistry
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.matcher.mapping.IsMappingOutputEqual.isMappingOutputEqual
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ToViewIntentMappingTest {

    @Test
    fun checkIfToViewIntentMappingOutputIsEqual() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext!!
        val input = R.string.url_about

        assertThat(
            ToViewIntentMapping(context),
            isMappingOutputEqual(
                input,
                ViewIntent(
                    Uri.parse(
                        context.getString(
                            input
                        )
                    )
                )
            )
        )
    }
}