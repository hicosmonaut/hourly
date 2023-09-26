/*
 *  MIT License
 *
 *  Copyright (c) 2023 Denis Kholodenin, hi.cosmonaut@gmail.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package hi.cosmonaut.hourly.picker.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object TimePicker {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Dialog(
        title: String,
        timePickerState: TimePickerState,
        onConfirmed: () -> Unit,
        onCancel: () -> Unit,
    ) {
        AlertDialog(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 28.dp)
                ),
            onDismissRequest = onCancel
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                    )
            ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(
                            vertical = 8.dp,
                            horizontal = 32.dp
                        ),
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                TimePicker(
                    modifier = Modifier
                        .padding(
                            vertical = 8.dp
                        )
                        .align(
                            alignment = Alignment.CenterHorizontally
                        ),
                    state = timePickerState
                )

                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 4.dp
                        )
                        .align(
                            alignment = Alignment.End
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier
                            .padding(
                                horizontal = 8.dp
                            ),
                        onClick = onCancel
                    ) {
                        Text(
                            text = stringResource(android.R.string.cancel),
                            textAlign = TextAlign.End
                        )
                    }

                    TextButton(
                        modifier = Modifier
                            .padding(
                                horizontal = 8.dp
                            ),
                        onClick = onConfirmed
                    ) {
                        Text(
                            text = stringResource(android.R.string.ok),
                        )
                    }
                }
            }
        }
    }


    @Stable
    class DialogState private constructor(
        private val state: MutableState<Boolean>,
    ) {

        constructor(
            currentState: Boolean = false,
        ) : this(
            mutableStateOf(currentState)
        )

        fun markAsClosed() {
            state.value = false
        }

        fun markAsOpened() {
            state.value = true
        }

        fun opened() = state.value
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DialogState

            if (state.value != other.state.value) return false

            return true
        }

        override fun hashCode(): Int = 38 * state.value.hashCode()


    }

    @Composable
    fun rememberDialogState(
        opened: Boolean = false,
    ): DialogState = remember {
        DialogState(
            opened
        )
    }
}