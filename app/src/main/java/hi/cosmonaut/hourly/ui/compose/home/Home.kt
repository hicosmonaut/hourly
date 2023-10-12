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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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

            NoticeCard(
                iconPainter = painterResource(id = R.drawable.icon_alert),
                text = stringResource(id = R.string.text_notification_info),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
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
    fun NoticeCard(
        iconPainter: Painter,
        text: String,
        containerColor: Color,
        contentColor: Color,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = contentColor
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = contentColor
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
                    vertical = 8.dp
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp
                    )
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    ),
                    text = stringResource(
                        id = R.string.label_set_time
                    ),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(8.dp))

                TimeOutlinedTextField(
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

                TimeOutlinedTextField(
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

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(
                                horizontal = 8.dp
                            ),
                        onClick = OnCancelAllClick(context)
                    ) {
                        Text(
                            text = stringResource(id = R.string.label_cancel_all),
                        )
                    }
                    Spacer(
                        modifier = Modifier.width(16.dp)
                    )
                    TextButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(
                                horizontal = 8.dp
                            ),
                        onClick = OnApplyClick(context, startTime, endTime)
                    ) {
                        Text(
                            text = stringResource(id = R.string.label_apply),
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun TimeOutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit = {},
        enabled: Boolean = false,
        @StringRes labelResId: Int,
        @DrawableRes leadingIconResId: Int,
        @DrawableRes trailingIconResId: Int,
        colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick: () -> Unit,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                },
            enabled = enabled,
            label = {
                Text(
                    text = stringResource(id = labelResId),
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = leadingIconResId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(id = trailingIconResId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            colors = colors,
            value = value,
            onValueChange = onValueChange
        )
    }
}