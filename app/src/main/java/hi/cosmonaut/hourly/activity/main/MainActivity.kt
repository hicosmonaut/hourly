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

package hi.cosmonaut.hourly.activity.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModel
import hi.cosmonaut.hourly.fragment.home.vm.HomeViewModelFactory
import hi.cosmonaut.hourly.tool.back.BackHandler
import hi.cosmonaut.hourly.ui.compose.home.Home
import hi.cosmonaut.hourly.ui.compose.splash.Splash
import hi.cosmonaut.hourly.ui.theme.HourlyTheme
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var i = 1
        setContent {

            val controller = rememberNavController()

            HourlyTheme(
                darkTheme = false
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            vertical = 16.dp
                        ),
                    color = MaterialTheme.colorScheme.background
                ){
                    NavHost(
                        navController = controller,
                        startDestination = "splash"
                    ) {
                        composable(
                            route = "splash"
                        ) {
                            Splash.Screen(
                                navController = controller
                            )
                        }
                        composable(
                            route = "home"
                        ) {

                            BackHandler.Empty()

                            val homeViewModel: HomeViewModel = viewModel(
                                factory = HomeViewModelFactory(application)
                            )

                            val startTime by homeViewModel.startTime.collectAsStateWithLifecycle()
                            val endTime by homeViewModel.endTime.collectAsStateWithLifecycle()

                            Home.Screen(
                                startTime,
                                endTime,
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}