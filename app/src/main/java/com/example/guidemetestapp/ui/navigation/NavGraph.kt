package com.example.guidemetestapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.guidemetestapp.ui.screens.*
import com.example.guidemetestapp.presentation.auth.*

@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onFinish = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.ResetPassword.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) {
            ResetPasswordScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            MainScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                },
                onNavigateToTourMap = {
                    navController.navigate(Screen.TourMap.route)
                },
                onNavigateToTourPackages = {
                    navController.navigate(Screen.TourPackages.route)
                },
                onNavigateToMyBookings = {
                    navController.navigate(Screen.MyBookings.route)
                },
                onNavigateToAIChat = {
                    navController.navigate(Screen.AIChat.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.AIChat.route) {
            AIChatScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.TourMap.route,
            arguments = listOf(navArgument("tourId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tourId = backStackEntry.arguments?.getInt("tourId") ?: 0
            TourMapScreen(
                tourId = tourId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.TourPackages.route) {
            TourPackagesScreen(
                onBack = { navController.popBackStack() },
                onPackageClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                }
            )
        }
        composable(Screen.ResortSelection.route) {
            ResortSelectionScreen(
                onBack = { navController.popBackStack() },
                onResortSelected = { resortName ->
                    // Set result in previous backstack entry
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_resort", resortName)
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            DetailScreen(itemId = itemId, onBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.MyBookings.route) {
            MyBookingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                }
            )
        }
    }
}
