package hi.cosmonaut.hourly.fragment.home.repository

import hi.cosmonaut.hourly.proto.UserPreferences
import kotlinx.coroutines.flow.Flow

interface TimeRepository {
    suspend fun flowWithDefaultValues(): Flow<UserPreferences>
}