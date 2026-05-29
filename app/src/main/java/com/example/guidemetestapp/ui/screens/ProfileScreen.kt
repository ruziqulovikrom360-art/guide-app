package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext

import com.example.guidemetestapp.data.api.UrlUtils
import com.example.guidemetestapp.data.model.User
import com.example.guidemetestapp.ui.viewmodel.GuideViewModel

@Composable
fun ProfileScreen(
    viewModel: GuideViewModel,
    onNavigateToMyBookings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val isDebuggable = remember {
        0 != (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        
        // Profile Image
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = UrlUtils.getFullUrl(currentUser?.profileImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
            )
            Surface(
                color = Color.Black,
                shape = CircleShape,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(currentUser?.name ?: "Guest User", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(currentUser?.email ?: "", color = Color.Gray, fontSize = 14.sp)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        ProfileMenuItem(icon = Icons.Default.Person, label = "Personal Info")
        ProfileMenuItem(icon = Icons.Default.CalendarToday, label = "My Bookings", onClick = onNavigateToMyBookings)
        ProfileMenuItem(icon = Icons.Default.Payment, label = "Payments")
        ProfileMenuItem(icon = Icons.Default.Settings, label = "Settings")
        ProfileMenuItem(icon = Icons.Default.Help, label = "Help Center")
        
        if (isDebuggable) {
            ProfileMenuItem(
                icon = Icons.Default.BugReport,
                label = "Developer Tools (Chucker)",
                onClick = {
                    context.startActivity(com.chuckerteam.chucker.api.Chucker.getLaunchIntent(context))
                }
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Log Out", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, modifier = Modifier.weight(1f), fontSize = 16.sp)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
