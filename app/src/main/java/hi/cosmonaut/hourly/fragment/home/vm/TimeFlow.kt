package hi.cosmonaut.hourly.fragment.home.vm

import hi.cosmonaut.hourly.proto.UserPreferences
import kotlinx.coroutines.flow.Flow

interface TimeFlow {
    suspend fun provideTimeFlow(): Flow<UserPreferences>
}