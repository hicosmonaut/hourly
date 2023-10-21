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

package hi.cosmonaut.hourly.ui.compose.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.fragment.home.listener.OnAboutClick
import hi.cosmonaut.hourly.fragment.home.listener.OnApplyClick
import hi.cosmonaut.hourly.fragment.home.listener.OnCancelAllClick
import hi.cosmonaut.hourly.picker.time.TimePicker
import hi.cosmonaut.hourly.proto.UserPreferences
import hi.cosmonaut.hourly.ui.compose.common.Common

object Home {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(
        startTime: Pair<Int, Int>,
        endTime: Pair<Int, Int>,
        startTimePickerOpenState: TimePicker.DialogState = TimePicker.rememberDialogState(),
        endTimePickerOpenState: TimePicker.DialogState = TimePicker.rememberDialogState(),
        onStartTimeConfirmed: (Int, Int) -> Unit,
        onEndTimeConfirmed: (Int, Int) -> Unit,
    ) {

        val context = LocalContext.current.applicationContext

        val startTimePickerState = rememberTimePickerState(
            initialHour = startTime.first,
            initialMinute = startTime.second,
            is24Hour = true
        )

        val endTimePickerState = rememberTimePickerState(
            initialHour = endTime.first,
            initialMinute = endTime.second,
            is24Hour = true
        )

        if (startTimePickerOpenState.opened()) {
            TimePicker.Dialog(
                title = stringResource(id = R.string.label_start_time),
                timePickerState = startTimePickerState,
                onConfirmed = {
                    onStartTimeConfirmed(startTimePickerState.hour, startTimePickerState.minute)
                    startTimePickerOpenState.markAsClosed()
                },
                onCancel = { startTimePickerOpenState.markAsClosed() }
            )
        }

        if (endTimePickerOpenState.opened()) {
            TimePicker.Dialog(
                title = stringResource(id = R.string.label_end_time),
                timePickerState = endTimePickerState,
                onConfirmed = {
                    onEndTimeConfirmed(endTimePickerState.hour, endTimePickerState.minute)
                    endTimePickerOpenState.markAsClosed()
                },
                onCancel = { endTimePickerOpenState.markAsClosed()}
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Common.NoticeCard(
                iconPainter = painterResource(id = R.drawable.icon_alert),
                text = stringResource(id = R.string.text_notification_info),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )

            Common.VerticalSpacer(8.dp)

            TimeRangeCard(
                startTime = startTime,
                endTime = endTime,
                onStartTimeClick = { startTimePickerOpenState.markAsOpened() },
                onEndTimeClick = { endTimePickerOpenState.markAsOpened() },
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    modifier = Modifier
                        .clickable(
                            onClick = OnAboutClick(context, R.string.url_about)
                        )
                        .alpha(0.5f),
                    text = stringResource(id = R.string.label_about),
                    style = TextStyle(textDecoration = TextDecoration.Underline)

                )
            }
        }
    }

    @Composable
    fun TimeRangeCard(
        startTime: Pair<Int, Int>,
        endTime: Pair<Int, Int>,
        onStartTimeClick: () -> Unit,
        onEndTimeClick: () -> Unit,
    ) {

        val context = LocalContext.current.applicationContext

        Card(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp,
                    )
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(
                            id = R.string.label_set_time
                        ),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    /*Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )*/
                }

                Common.TimeOutlinedTextField(
                    value = stringResource(
                        R.string.label_HH_mm,
                        startTime.first,
                        startTime.second
                    ),
                    labelResId = R.string.label_start_time,
                    leadingIconResId = R.drawable.icon_sun,
                    trailingIconResId = R.drawable.icon_time,
                    onClick = onStartTimeClick
                )

                Common.TimeOutlinedTextField(
                    value = stringResource(
                        R.string.label_HH_mm,
                        endTime.first,
                        endTime.second
                    ),
                    labelResId = R.string.label_end_time,
                    leadingIconResId = R.drawable.icon_moon,
                    trailingIconResId = R.drawable.icon_time,
                    onClick = onEndTimeClick
                )

                Common.VerticalSpacer(4.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    Common.DiscardButton(
                        onClick = OnCancelAllClick(context)
                    )
                    Common.HorizontalSpacer(16.dp)
                    Common.ApplyButton(
                        onClick = OnApplyClick(context, startTime, endTime)
                    )
                }

                Common.VerticalSpacer(8.dp)

            }
        }
    }

}