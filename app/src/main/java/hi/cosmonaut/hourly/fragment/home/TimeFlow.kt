package hi.cosmonaut.hourly.fragment.home

import hi.cosmonaut.hourly.proto.UserPreferences
import kotlinx.coroutines.flow.Flow

interface TimeFlow {
    suspend fun provideTimeFlow(): Flow<UserPreferences>
}