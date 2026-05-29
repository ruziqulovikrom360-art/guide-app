package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

sealed class NavigationState {
    object TourMap1 : NavigationState()
    object TourMap2 : NavigationState()
    object TourMap3 : NavigationState()
    object TourMap4 : NavigationState()
    object SearchState : NavigationState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen() {
    var currentState by remember { mutableStateOf<NavigationState>(NavigationState.TourMap1) }

    Scaffold(
        topBar = {
            // Full map holatlarida TopBar ko'rinmaydi
            if (currentState != NavigationState.TourMap2 && currentState != NavigationState.TourMap3) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = "https://picsum.photos/seed/mafruza/100/100",
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Mafruza", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Notifications, contentDescription = null)
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentState) {
                NavigationState.TourMap1 -> TourMapContent(
                    padding = padding,
                    buttonText = "Start tour",
                    onMapClick = { currentState = NavigationState.TourMap2 },
                    onButtonClick = { currentState = NavigationState.TourMap2 },
                    onTabSwitch = { currentState = NavigationState.SearchState }
                )
                NavigationState.TourMap2 -> FullMapContent(
                    imageUrl = "https://picsum.photos/seed/fullmap1/1200/1800",
                    onClose = { currentState = NavigationState.TourMap3 }
                )
                NavigationState.TourMap3 -> FullMapContent(
                    imageUrl = "https://picsum.photos/seed/fullmap2/1200/1800",
                    onClose = { currentState = NavigationState.TourMap4 }
                )
                NavigationState.TourMap4 -> TourMapContent(
                    padding = padding,
                    buttonText = "End tour",
                    onMapClick = { currentState = NavigationState.TourMap1 },
                    onButtonClick = { currentState = NavigationState.SearchState },
                    onTabSwitch = { currentState = NavigationState.SearchState }
                )
                NavigationState.SearchState -> NavigationSearchContent(
                    padding = padding,
                    onTabSwitch = { currentState = NavigationState.TourMap1 }
                )
            }
        }
    }
}

@Composable
fun TourMapContent(
    padding: PaddingValues,
    buttonText: String,
    onMapClick: () -> Unit,
    onButtonClick: () -> Unit,
    onTabSwitch: () -> Unit
) {
    Column(modifier = Modifier.padding(padding).fillMaxSize()) {
        NavigationTabs(isTourMap = true, onTabSwitch = onTabSwitch)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            AsyncImage(
                model = "https://picsum.photos/seed/tourmap/800/600",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Button(
                onClick = onButtonClick,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp).fillMaxWidth(0.9f).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(buttonText, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                ItineraryItem(
                    time = "09:00-11:00",
                    title = "TUIT University",
                    description = "Erkak va hayot uyg'unlashgan va go'zal istirohat bog'",
                    icon = Icons.Outlined.School,
                    isFirst = true
                )
            }
            item {
                ItineraryItem(
                    time = "11:30-12:30",
                    title = "Beshqozon",
                    description = "Try everything natural and fresh, like pilaf from Beshqozon",
                    icon = Icons.Outlined.Restaurant
                )
            }
            item {
                ItineraryItem(
                    time = "11:00-12:30",
                    title = "Magic City",
                    description = "Mo'jizalar zavqini Alpomish bilan",
                    icon = Icons.Outlined.Star,
                    isLast = true
                )
            }
        }
    }
}

@Composable
fun FullMapContent(imageUrl: String, onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        IconButton(
            onClick = onClose,
            modifier = Modifier.padding(24.dp).align(Alignment.TopEnd).background(Color.White, CircleShape)
        ) {
            Icon(Icons.Default.Close, null)
        }
    }
}

@Composable
fun NavigationSearchContent(padding: PaddingValues, onTabSwitch: () -> Unit) {
    Column(modifier = Modifier.padding(padding).fillMaxSize().background(Color.White)) {
        NavigationTabs(isTourMap = false, onTabSwitch = onTabSwitch)
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                placeholder = { Text("Search", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                trailingIcon = { Icon(Icons.Outlined.Mic, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun NavigationTabs(isTourMap: Boolean, onTabSwitch: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF3F3F3),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Row {
                TabItem("Tour map", isSelected = isTourMap, onClick = { if (!isTourMap) onTabSwitch() })
                TabItem("Navigation", isSelected = !isTourMap, onClick = { if (isTourMap) onTabSwitch() })
            }
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) Color.Black else Color.Transparent,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ItineraryItem(
    time: String, 
    title: String, 
    description: String, 
    icon: ImageVector,
    isFirst: Boolean = false, 
    isLast: Boolean = false
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = icon, 
                    contentDescription = null, 
                    modifier = Modifier.padding(6.dp),
                    tint = Color.Black
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(Color.LightGray)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(time, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Text(description, color = Color.Gray, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}
