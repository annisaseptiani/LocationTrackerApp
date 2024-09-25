package com.example.locationtrackingapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.presentation.view.HistoryScreen.HistoryScreen
import com.example.presentation.view.LiveTrackingScreen.LiveTrackingScreen
import com.example.presentation.view.LiveTrackingScreen.LiveTrackingViewModel
import com.example.presentation.view.HistoryScreen.LocationViewModel

@Composable
fun NavigationGraph(modifier : Modifier,
                    navHostController : NavHostController,
                    viewModel: LocationViewModel,
                    trackingViewModel: LiveTrackingViewModel
) {

    NavHost(navController = navHostController, startDestination = Routes.Home.routes) {
       composable(Routes.Home.routes) {
           LiveTrackingScreen(viewModel= trackingViewModel, onNavigateToHistory = { navHostController.navigate(Routes.History.routes) })
       }
        composable(Routes.History.routes) {
            HistoryScreen(viewModel = viewModel)
        }
    }
}