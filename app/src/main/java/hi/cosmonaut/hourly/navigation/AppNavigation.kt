package hi.cosmonaut.hourly.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.createGraph
import hi.cosmonaut.hourly.ui.compose.home.homeScreen
import hi.cosmonaut.hourly.ui.compose.home.navigateToHome
import hi.cosmonaut.hourly.ui.compose.splash.splashScreen

@Composable
fun NavController.mainGraph(startDestination : String) =
    this.createGraph(
        startDestination = startDestination,
        route = "app"
    ){
        this.splashScreen {
            this@mainGraph.navigateToHome()
        }
        this.homeScreen()
    }