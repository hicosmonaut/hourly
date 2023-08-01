package hi.cosmonaut.hourly.fragment.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(app) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return viewModel
    }
}