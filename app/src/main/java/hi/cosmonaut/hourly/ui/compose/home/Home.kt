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

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.ui.compose.home.listener.OnAboutClick
import hi.cosmonaut.hourly.ui.compose.home.vm.HomeViewModel
import hi.cosmonaut.hourly.ui.compose.home.vm.HomeViewModelFactory
import hi.cosmonaut.hourly.picker.time.TimePicker
import hi.cosmonaut.hourly.tool.back.BackHandler
import hi.cosmonaut.hourly.ui.compose.common.Common
import hi.cosmonaut.hourly.ui.compose.common.Common.TimeColumn
import hi.cosmonaut.hourly.ui.compose.home.Home.Screen
import hi.cosmonaut.hourly.ui.theme.HourlyTheme
import kotlinx.coroutines.launch

object Home {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(
        startTime: Pair<Int, Int>,
        endTime: Pair<Int, Int>,
        alarmsEnabled: Boolean,
        permissionGranted: Boolean,
        startTimePickerOpenState: TimePicker.DialogState = TimePicker.rememberDialogState(),
        endTimePickerOpenState: TimePicker.DialogState = TimePicker.rememberDialogState(),
        onStartTimeConfirmed: (Int, Int) -> Unit,
        onEndTimeConfirmed: (Int, Int) -> Unit,
        onEnableAlarmsClicked: (Boolean) -> Unit = {},
        onGrantClicked: () -> Unit = {},
    ) {
        val alarmsEnabledCondition = alarmsEnabled && permissionGranted
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
                onCancel = { endTimePickerOpenState.markAsClosed() }
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.padding(vertical = 8.dp),
                painter = painterResource(id = R.drawable.icon_logo),
                contentDescription = ""
            )

            if (!permissionGranted) {
                Common.PermissionCard(
                    imageVector = Icons.Filled.Warning,
                    text = stringResource(id = R.string.text_grant_permission_notification),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error,
                    onGrantClicked = onGrantClicked
                )
                Common.VerticalSpacer(16.dp)
            }

            TimeRangeCard(
                alarmsEnabled = alarmsEnabledCondition,
                permissionGranted = permissionGranted,
                startTime = startTime,
                endTime = endTime,
                onStartTimeClick = { if (alarmsEnabledCondition) startTimePickerOpenState.markAsOpened() },
                onEndTimeClick = { if (alarmsEnabledCondition) endTimePickerOpenState.markAsOpened() },
                onEnableAlarmsClick = { if (permissionGranted) onEnableAlarmsClicked(it) }
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
        alarmsEnabled: Boolean,
        permissionGranted: Boolean,
        startTime: Pair<Int, Int>,
        endTime: Pair<Int, Int>,
        onStartTimeClick: () -> Unit,
        onEndTimeClick: () -> Unit,
        onEnableAlarmsClick: (Boolean) -> Unit = {},
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        val alpha = if (alarmsEnabled) 1f else 0.5f

        Card(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                )
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                    )
                ),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TimeColumn(
                        modifier = Modifier
                            .weight(1f)
                            .alpha(alpha),
                        titleResId = R.string.label_start_time,
                        hours = startTime.first,
                        minutes = startTime.second,
                    )
                    TimeColumn(
                        modifier = Modifier
                            .weight(1f)
                            .alpha(alpha),
                        titleResId = R.string.label_end_time,
                        hours = endTime.first,
                        minutes = endTime.second,
                    )
                    IconButton(
                        modifier = Modifier
                            .alpha(0.85f),
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = null
                        )
                    }
                }

                if (expanded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(
                                R.string.label_enable_alarms
                            ),
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                        Switch(
                            checked = alarmsEnabled && permissionGranted,
                            onCheckedChange = { onEnableAlarmsClick(it) },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                uncheckedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Common.TimeOutlinedTextField(
                        modifier = Modifier.alpha(alpha),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Common.TimeOutlinedTextField(
                        modifier = Modifier.alpha(alpha),
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

                    Spacer(modifier = Modifier.height(8.dp))

                }

            }
        }
    }

}


@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.homeScreen() {
    composable("app/home") {
        val context = LocalContext.current.applicationContext
        val lifecycleOwner = LocalLifecycleOwner.current

        BackHandler.Empty()

        val homeViewModel: HomeViewModel = viewModel(
            factory = HomeViewModelFactory(context as Application)
        )

        val postPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        val startTime by homeViewModel.startTime.collectAsStateWithLifecycle()
        val endTime by homeViewModel.endTime.collectAsStateWithLifecycle()
        val alarmsEnabled by homeViewModel.alarmsEnabledFlow.collectAsStateWithLifecycle()
        val permissionGranted = postPermissionState.status.isGranted

        LaunchedEffect(Unit) {
            lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                homeViewModel.updateAlarmsEnabled(alarmsEnabled && permissionGranted)
                Log.d(
                    "Home",
                    "homeScreen: alarmsEnabled: $alarmsEnabled, permissionGranted: $permissionGranted"
                )
            }
        }

        Screen(
            startTime = startTime,
            endTime = endTime,
            alarmsEnabled = alarmsEnabled,
            permissionGranted = permissionGranted,
            onStartTimeConfirmed = { hour, minute ->
                homeViewModel.updateStartTime(
                    hour,
                    minute
                )
            },
            onEndTimeConfirmed = { hour, minute ->
                homeViewModel.updateEndTime(
                    hour,
                    minute
                )
            },
            onEnableAlarmsClicked = { homeViewModel.updateAlarmsEnabled(it) },
            onGrantClicked = { postPermissionState.launchPermissionRequest() }
        )
    }
}

fun NavController.navigateToHome() {
    this.navigate("app/home")
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    HourlyTheme {
        Screen(
            9 to 0,
            22 to 0,
            false,
            false,
            onStartTimeConfirmed = { _, _ -> },
            onEndTimeConfirmed = { _, _ -> },
            onEnableAlarmsClicked = { _ -> }
        )
    }
}

