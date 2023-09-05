package hi.cosmonaut.hourly.tool.extension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import hi.cosmonaut.hourly.data.store.local.proto.UserPreferencesSerializer
import hi.cosmonaut.hourly.proto.UserPreferences

object ContextExtension {

    val Context.userDataStore: DataStore<UserPreferences> by dataStore(
        fileName ="user_prefs.json",
        serializer = UserPreferencesSerializer
    )
}