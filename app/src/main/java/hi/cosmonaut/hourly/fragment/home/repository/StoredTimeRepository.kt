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
}