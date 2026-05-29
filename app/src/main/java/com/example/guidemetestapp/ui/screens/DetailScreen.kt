package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.guidemetestapp.data.api.UrlUtils
import com.example.guidemetestapp.presentation.detail.DetailSideEffect
import com.example.guidemetestapp.presentation.detail.DetailViewModel
import com.example.guidemetestapp.ui.components.FullScreenLoading
import com.example.guidemetestapp.ui.components.GlassyCard
import com.example.guidemetestapp.ui.components.PremiumButton
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun DetailScreen(
    itemId: String, 
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DetailSideEffect.NavigateBack -> onBack()
            is DetailSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_LONG).show()
            }
            is DetailSideEffect.ShowMessage -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            DetailSideEffect.BookingSuccess -> {
                // Maybe navigate to a success screen or show a dialog
            }
        }
    }

    LaunchedEffect(itemId) {
        viewModel.loadDetail(itemId)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        val name = state.property?.nameEn ?: state.tour?.nameEn ?: state.region?.nameEn
        val description = state.property?.descriptionEn ?: state.tour?.descriptionEn ?: state.region?.description
        val imageUrl = state.property?.coverUrl ?: state.tour?.coverUrl ?: state.region?.coverUrl
        val fullImageUrl = UrlUtils.getFullUrl(imageUrl)
        val rating = state.property?.ratingGuest ?: 0.0
        val location = state.property?.address ?: state.region?.nameEn ?: ""

        if (name != null) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Box(modifier = Modifier.height(400.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = fullImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
                    )
                    
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                                    startY = 600f
                                )
                            )
                    )
                }
                
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .offset(y = (-40).dp)
                ) {
                    GlassyCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                    Text(" $location", fontSize = 14.sp, color = Color.Gray)
                                }
                            }
                            if (rating > 0) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFFB400).copy(alpha = 0.1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB400), modifier = Modifier.size(16.dp))
                                        Text(" $rating", fontWeight = FontWeight.Bold, color = Color(0xFFFFB400))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("About", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description ?: "No description available.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = Color.DarkGray
                    )

                    state.property?.let { property ->
                        Spacer(modifier = Modifier.height(24.dp))
                        PropertySpecifics(property)
                    }

                    state.tour?.let { tour ->
                        Spacer(modifier = Modifier.height(24.dp))
                        TourSpecifics(tour)
                    }

                    if (state.reviews.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Reviews", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        state.reviews.forEach { review ->
                            ReviewItem(review)
                        }
                    }

                    // Add Review Section
                    state.property?.let {
                        Spacer(modifier = Modifier.height(24.dp))
                        AddReviewSection(onAddReview = { rating, comment ->
                            viewModel.addReview(rating, comment)
                        })
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    if (state.region == null) {
                        PremiumButton(
                            text = if (state.isBookingInProgress) "Processing..." else if (state.tour != null) "Join Tour" else "Book Visit",
                            enabled = !state.isBookingInProgress,
                            onClick = {
                                if (state.property != null && state.property!!.units.isNotEmpty()) {
                                    // For simplicity, booking the first unit
                                    val unit = state.property!!.units.first()
                                    viewModel.bookProperty(
                                        unitId = unit.id,
                                        checkIn = "2024-06-01", // Placeholder
                                        checkOut = "2024-06-05", // Placeholder
                                        guests = 2
                                    )
                                }
                            }
                        )
                    }
                }
            }
        } else if (!state.isLoading && state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${state.error}", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadDetail(itemId) }) {
                        Text("Retry")
                    }
                }
            }
        }

        if (state.isLoading) {
            FullScreenLoading()
        }

            // Top Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        .shadow(2.dp, CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black, modifier = Modifier.size(20.dp))
                }
                
                IconButton(
                    onClick = { viewModel.toggleFavorite() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        .shadow(2.dp, CircleShape),
                    enabled = state.property != null || state.tour != null
                ) {
                    Icon(
                        if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (state.isFavorite) Color.Red else Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun AddReviewSection(onAddReview: (Int, String) -> Unit) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text("Leave a Review", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            repeat(5) { index ->
                IconButton(onClick = { rating = index + 1 }) {
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = if (index < rating) Color(0xFFFFB400) else Color.Gray
                    )
                }
            }
        }
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Write your experience...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                if (comment.isNotBlank()) {
                    onAddReview(rating, comment)
                    comment = ""
                }
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun ReviewItem(review: com.example.guidemetestapp.data.model.Review) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = if (!review.userAvatar.isNullOrEmpty()) com.example.guidemetestapp.data.api.UrlUtils.getFullUrl(review.userAvatar) else "https://ui-avatars.com/api/?name=${review.userName}",
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(review.userName ?: "Unknown User", fontWeight = FontWeight.Bold)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = if (index < review.rating) Color(0xFFFFB400) else Color.LightGray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(review.createdAt.take(10), fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.textEn ?: "", fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun PropertySpecifics(property: com.example.guidemetestapp.data.model.Property) {
    if (property.units.isNotEmpty()) {
        Text("Options", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        property.units.forEach { unit ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(unit.nameEn, fontWeight = FontWeight.Bold)
                        Text("${unit.unitType} • Max ${unit.maxGuests ?: 2} guests", fontSize = 12.sp, color = Color.Gray)
                    }
                    Text("$${unit.basePrice.toInt()}", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun TourSpecifics(tour: com.example.guidemetestapp.data.model.Tour) {
    Text("Itinerary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    tour.stops.sortedBy { it.stopOrder }.forEach { stop ->
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(stop.stopOrder.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Stop ${stop.stopOrder}", fontWeight = FontWeight.Bold)
                stop.noteEn?.let { Text(it, fontSize = 14.sp, color = Color.Gray) }
            }
        }
    }
}
