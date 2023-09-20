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

package hi.cosmonaut.hourly.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hi.cosmonaut.hourly.R
import hi.cosmonaut.hourly.ui.compose.home.Home
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModel
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModelFactory
import hi.cosmonaut.hourly.picker.time.TimePicker
import hi.cosmonaut.hourly.tool.back.BackHandler
import hi.cosmonaut.hourly.ui.theme.HourlyTheme

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>{
        HomeViewModelFactory(
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HourlyTheme(
                    darkTheme = false
                ) {
                    BackHandler.Empty()

                    val prefs by viewModel.timeFlow.collectAsStateWithLifecycle(lifecycleOwner = viewLifecycleOwner)
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

                        Home.NoticeCard(
                            iconPainter = painterResource(id = R.drawable.icon_alert),
                            text = stringResource(id = R.string.text_notification_info),
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                        Home.TimeRangeCard(
                            prefs = prefs,
                            startTimePickerState = startTimePickerState,
                            endTimePickerState = endTimePickerState
                        )

                        LaunchedEffect("home") {
                            viewModel.launch()
                        }
                    }
                }
            }
        }
    }

}