package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.guidemetestapp.ui.navigation.Screen
import com.example.guidemetestapp.ui.viewmodel.GuideViewModel

@Composable
fun MainScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToTourMap: (Int) -> Unit,
    onNavigateToTourPackages: () -> Unit,
    onNavigateToMyBookings: () -> Unit,
    onNavigateToAIChat: () -> Unit,
    onLogout: () -> Unit,
    viewModel: GuideViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Planner,
        Screen.AIChat,
        Screen.Navigation,
        Screen.Favorites
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp)),
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Home -> if (isSelected) Icons.Default.Home else Icons.Outlined.Home
                                    Screen.Planner -> if (isSelected) Icons.Default.DateRange else Icons.Outlined.DateRange
                                    Screen.AIChat -> if (isSelected) Icons.Default.ChatBubble else Icons.Outlined.ChatBubbleOutline
                                    Screen.Navigation -> if (isSelected) Icons.Default.Explore else Icons.Outlined.Explore
                                    Screen.Favorites -> if (isSelected) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder
                                    else -> Icons.Default.Home
                                },
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = when(screen) {
                                    Screen.Home -> "Home"
                                    Screen.Planner -> "Planner"
                                    Screen.AIChat -> "AI-chat"
                                    Screen.Navigation -> "Navigation"
                                    Screen.Favorites -> "Saved"
                                    else -> ""
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            if (screen == Screen.AIChat) {
                                onNavigateToAIChat()
                            } else {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color.Transparent,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel, 
                    onNavigateToDetail = onNavigateToDetail,
                    onSeeAllPackages = onNavigateToTourPackages,
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }
            composable(Screen.Planner.route) {
                PlannerScreen(onNavigateToTourMap = onNavigateToTourMap)
            }
            composable(Screen.Navigation.route) {
                NavigationScreen()
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(viewModel = viewModel, onNavigateToDetail = onNavigateToDetail)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onLogout = onLogout,
                    onNavigateToMyBookings = onNavigateToMyBookings
                )
            }
        }
    }
}
