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

package hi.cosmonaut.hourly.fragment.home.repository

import androidx.datastore.core.DataStore
import hi.cosmonaut.hourly.proto.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class StoredTimeRepository(
    private val store: DataStore<UserPreferences>
): TimeRepository {
    override suspend fun flowWithDefaultValues(): Flow<UserPreferences> = store.data
        .onEach {
            val builder = it.toBuilder()

            if (!it.defaultApplied) {
                builder.startHours = 9
                builder.startMinutes = 0
                builder.endHours = 21
                builder.endMinutes = 0
                builder.defaultApplied = true
            }

            builder.build()
        }

    override suspend fun updateStartTime(hour: Int, minute: Int) {
        store.updateData {
            it.toBuilder()
                .setStartMinutes(minute)
                .setStartHours(hour)
                .build()
        }
    }

    override suspend fun updateEndTime(hour: Int, minute: Int) {
        store.updateData {
            it.toBuilder()
                .setEndMinutes(minute)
                .setEndHours(hour)
                .build()
        }
    }
}