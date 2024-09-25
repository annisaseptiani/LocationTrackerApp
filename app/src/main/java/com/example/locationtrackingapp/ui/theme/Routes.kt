package com.example.locationtrackingapp.ui.theme

sealed class Routes(val routes : String) {
    object Home : Routes ("home")
    object History : Routes("history")
}