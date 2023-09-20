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

import android.app.Application
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.fragment.home.listener.OnAboutClick
import hi.cosmonaut.hourly.fragment.home.listener.OnApplyClick
import hi.cosmonaut.hourly.fragment.home.listener.OnCancelAllClick
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModel
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModelFactory
import hi.cosmonaut.hourly.picker.time.TimePicker
import hi.cosmonaut.hourly.proto.UserPreferences
import hi.cosmonaut.hourly.tool.back.BackHandler

object Home {

    @Composable
    fun Screen(
        application: Application,
        navController: NavHostController,
        viewModel: HomeViewModel = viewModel(
            factory = HomeViewModelFactory(application)
        ),
    ){
        BackHandler.Empty()

        val prefs by viewModel.timeFlow.collectAsStateWithLifecycle()
        val startTimePickerState = remember { mutableStateOf(false) }
        val endTimePickerState = remember { mutableStateOf(false) }

        if (startTimePickerState.value) {
            TimePicker.Dialog(
                title = stringResource(id = R.string.label_start_time),
                startTimePickerState,
                initialHour = prefs.startHours,
                initialMinute = prefs.startMinutes,
                onConfirmed = { hour, minute ->
                    viewModel.updateStartTime(hour, minute)
                },
                onCancel = { }
            )
        }

        if (endTimePickerState.value) {
            TimePicker.Dialog(
                title = stringResource(id = R.string.label_end_time),
                endTimePickerState,
                initialHour = prefs.endHours,
                initialMinute = prefs.endMinutes,
                onConfirmed = { hour, minute ->
                    viewModel.updateEndTime(hour, minute)
                },
                onCancel = {}
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
                prefs = prefs,
                startTimePickerState = startTimePickerState,
                endTimePickerState = endTimePickerState
            )

            LaunchedEffect("home") {
                viewModel.launch()
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
        prefs: UserPreferences,
        startTimePickerState: MutableState<Boolean>,
        endTimePickerState: MutableState<Boolean>
    ){

        val context = LocalContext.current.applicationContext
        val scope = rememberCoroutineScope()

        Card(
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                        prefs.startHours,
                        prefs.startMinutes
                    ),
                    labelResId = R.string.label_start_time,
                    leadingIconResId = R.drawable.icon_sun,
                    trailingIconResId = R.drawable.icon_time,
                    onClick = {
                        startTimePickerState.value = true
                    }
                )

                TimeOutlinedTextField(
                    value = stringResource(
                        R.string.label_HH_mm,
                        prefs.endHours,
                        prefs.endMinutes
                    ),
                    labelResId = R.string.label_end_time,
                    leadingIconResId = R.drawable.icon_moon,
                    trailingIconResId = R.drawable.icon_time,
                    onClick = {
                        endTimePickerState.value = true
                    }
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
                        onClick = OnCancelAllClick(context.applicationContext)
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
                        onClick = OnApplyClick(context, scope)
                    ) {
                        Text(
                            text = stringResource(id = R.string.label_apply),
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            val aboutOnClick = OnAboutClick(context, R.string.url_about)

            Text(
                modifier = Modifier.clickable {
                    aboutOnClick()
                },
                text = stringResource(id = R.string.label_about),
                style = TextStyle(textDecoration = TextDecoration.Underline)

            )
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