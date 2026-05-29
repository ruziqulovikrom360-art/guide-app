package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.guidemetestapp.data.model.AISuggestion
import com.example.guidemetestapp.ui.components.GlassyCard
import com.example.guidemetestapp.ui.components.PremiumButton
import com.example.guidemetestapp.ui.components.SearchBarPremium
import com.example.guidemetestapp.ui.viewmodel.AIChatViewModel

data class AIChatUiMessage(
    val text: String,
    val isUser: Boolean,
    val hasImage: Boolean = false,
    val tourPackage: AIChatTourPackage? = null,
    val suggestions: List<AISuggestion> = emptyList(),
    val weather: Map<String, Any>? = null
)

data class AIChatTourPackage(
    val title: String,
    val price: String,
    val duration: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen(
    onBack: () -> Unit = {},
    viewModel: AIChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "https://picsum.photos/seed/ai/100/100",
                            contentDescription = null,
                            modifier = Modifier.size(40.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("GuideMe-AI", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message)
                }
                if (isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }

            // Input Area
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    ChatChip("Cheap")
                    ChatChip("Customizable")
                    ChatChip("Comfortable")
                }

                SearchBarPremium(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = "Ask anything about properties...",
                    leadingIcon = {
                        IconButton(onClick = { /* Open camera */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.Gray)
                        }
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { /* Voice input */ }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (inputText.isNotBlank()) {
                                        viewModel.sendMessage(inputText)
                                        inputText = ""
                                    }
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Black, CircleShape)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: AIChatUiMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        if (message.hasImage) {
            Box(
                modifier = Modifier
                    .width(240.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0))
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto, 
                    contentDescription = null, 
                    modifier = Modifier.size(60.dp).align(Alignment.Center),
                    tint = Color.Gray.copy(alpha = 0.5f)
                )
            }
        } else if (message.tourPackage != null) {
            TourPackageCard(message.tourPackage)
        } else {
            Surface(
                color = if (message.isUser) Color(0xFF6B5E71) else Color(0xFFEEE8F4),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isUser) 16.dp else 0.dp,
                    bottomEnd = if (message.isUser) 0.dp else 16.dp
                )
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = if (message.isUser) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }

        if (message.weather != null) {
            Spacer(modifier = Modifier.height(8.dp))
            WeatherCard(message.weather)
        }

        if (message.suggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(message.suggestions) { suggestion ->
                    AISuggestionCard(suggestion)
                }
            }
        }
    }
}

@Composable
fun WeatherCard(weather: Map<String, Any>) {
    val city = weather["city"]?.toString() ?: "Unknown"
    val temp = weather["temp"]?.toString() ?: "--"
    val description = weather["description"]?.toString() ?: ""
    val iconUrl = weather["icon_url"]?.toString()

    GlassyCard(
        modifier = Modifier.width(200.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(city, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("$temp°C", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
            if (iconUrl != null) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun AISuggestionCard(suggestion: AISuggestion) {
    GlassyCard(
        modifier = Modifier.width(200.dp),
        elevation = 4.dp
    ) {
        Column {
            Text(suggestion.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
            Text(suggestion.propertyType, color = Color.Gray, fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB400), modifier = Modifier.size(12.dp))
                Text(" ${suggestion.rating}", fontSize = 12.sp)
            }
            Text(suggestion.description, fontSize = 11.sp, maxLines = 2, color = Color.DarkGray)
        }
    }
}

@Composable
fun TourPackageCard(tourPackage: AIChatTourPackage) {
    GlassyCard(
        modifier = Modifier.width(280.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = "https://picsum.photos/seed/${tourPackage.title}/400/200",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(tourPackage.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(tourPackage.price, fontWeight = FontWeight.Bold, color = Color(0xFF6B5E71))
                }
                Text(tourPackage.duration, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(tourPackage.description, fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                PremiumButton(
                    text = "Select Package",
                    onClick = { /* Select package */ },
                    modifier = Modifier.height(44.dp)
                )
            }
        }
    }
}

@Composable
fun ChatChip(label: String) {
    Surface(
        color = Color(0xFFF3F3F3),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
