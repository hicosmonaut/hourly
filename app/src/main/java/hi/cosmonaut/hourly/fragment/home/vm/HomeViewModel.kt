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

package hi.cosmonaut.hourly.fragment.home.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import hi.cosmonaut.hourly.fragment.home.repository.TimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    app: Application,
    private val timeRepository: TimeRepository,
) : AndroidViewModel(app), TimeFlowAddon {

    private val _startTime: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(9 to 0)//fixme: pass default start time value
    override val startTime: StateFlow<Pair<Int, Int>> = _startTime.asStateFlow()

    private val _endTime: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(22 to 0)//fixme: pass default end time value
    override val endTime: StateFlow<Pair<Int, Int>> = _endTime.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                timeRepository.startTime().collect {
                    _startTime.emit(it)
                }
            }

            launch {
                timeRepository.endTime().collect {
                    _endTime.emit(it)
                }
            }
        }
    }

    override fun updateStartTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            timeRepository.updateStartTime(hour, minute)
        }
    }

    override fun updateEndTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            timeRepository.updateEndTime(hour, minute)
        }
    }

}