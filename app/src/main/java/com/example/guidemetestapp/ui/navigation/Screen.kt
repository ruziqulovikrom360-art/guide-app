package com.example.guidemetestapp.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object ResetPassword : Screen("reset_password/{token}") {
        fun createRoute(token: String) = "reset_password/$token"
    }
    object Home : Screen("home")
    object Planner : Screen("planner")
    object AIChat : Screen("ai_chat")
    object Navigation : Screen("navigation")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object MyBookings : Screen("my_bookings")
    object TourMap : Screen("tour_map/{tourId}") {
        fun createRoute(tourId: Int) = "tour_map/$tourId"
    }
    object TourPackages : Screen("tour_packages")
    object ResortSelection : Screen("resort_selection")
    
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: String) = "detail/$itemId"
    }
}
