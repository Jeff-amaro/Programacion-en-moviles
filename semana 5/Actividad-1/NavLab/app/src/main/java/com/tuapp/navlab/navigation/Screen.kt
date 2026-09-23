package com.tuapp.navlab.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object List : Screen("list_screen")
    object Detail : Screen("detail_screen/{itemId}")
    object Profile : Screen("profile_screen")
}