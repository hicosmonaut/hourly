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

package hi.cosmonaut.hourly.ui.compose.home.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import hi.cosmonaut.hourly.ui.compose.home.schedule.AlarmSchedule
import hi.cosmonaut.hourly.ui.compose.home.repository.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    app: Application,
    private val alarmRepository: AlarmRepository,
    private val alarmSchedule: AlarmSchedule
) : AndroidViewModel(app) {

    private val _startTime: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(9 to 0)//fixme: pass default start time value
    val startTime: StateFlow<Pair<Int, Int>> = _startTime.asStateFlow()

    private val _endTime: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(22 to 0)//fixme: pass default end time value
    val endTime: StateFlow<Pair<Int, Int>> = _endTime.asStateFlow()

    val alarmsEnabledFlow: StateFlow<Boolean> = alarmRepository.alarmsEnabled().stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        viewModelScope.launch {
            launch {
                alarmRepository.startTime().collect {
                    _startTime.emit(it)
                }
            }

            launch {
                alarmRepository.endTime().collect {
                    _endTime.emit(it)
                }
            }
        }
    }

    fun updateStartTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            alarmRepository.updateStartTime(hour, minute)
            updateSchedule()
        }
    }

    fun updateEndTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            alarmRepository.updateEndTime(hour, minute)
            updateSchedule()
        }
    }

    fun updateAlarmsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            alarmRepository.updateAlarmsEnabled(enabled)
            updateSchedule()
        }
    }

    private fun updateSchedule(){
        if(alarmsEnabledFlow.value){
            alarmSchedule.apply(
                startTime = startTime.value,
                endTime = endTime.value
            )
        } else {
            alarmSchedule.cancelAll()
        }
    }

}