package com.example.guidemetestapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.guidemetestapp.ui.navigation.NavGraph
import com.example.guidemetestapp.ui.theme.GuideMeTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.guidemetestapp.data.api.TokenManager
import com.example.guidemetestapp.ui.navigation.Screen
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Permission granted or denied
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        askNotificationPermission()
        
        enableEdgeToEdge()
        setContent {
            var startDestination by remember { mutableStateOf<String?>(null) }
            
            LaunchedEffect(Unit) {
                val token = tokenManager.accessToken.first()
                startDestination = if (!token.isNullOrEmpty()) {
                    Screen.Home.route
                } else {
                    Screen.Onboarding.route
                }
            }

            GuideMeTestAppTheme {
                if (startDestination != null) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController, startDestination = startDestination!!)
                }
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
