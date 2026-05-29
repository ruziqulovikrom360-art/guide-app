package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.guidemetestapp.data.api.UrlUtils
import com.example.guidemetestapp.data.model.Property
import com.example.guidemetestapp.data.model.Region
import com.example.guidemetestapp.data.model.Tour
import com.example.guidemetestapp.ui.viewmodel.GuideViewModel
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: GuideViewModel,
    onNavigateToDetail: (String) -> Unit,
    onSeeAllPackages: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val regions by viewModel.regions.collectAsState()
    val properties by viewModel.filteredProperties.collectAsState()
    val tours by viewModel.filteredTours.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favoritePropertyIds by viewModel.favoritePropertyIds.collectAsState()
    val favoriteTourIds by viewModel.favoriteTourIds.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = { viewModel.fetchAllData() },
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Custom Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f).clickable { onProfileClick() }
                    ) {
                        AsyncImage(
                            model = UrlUtils.getFullUrl(currentUser?.profileImageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Welcome",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = currentUser?.name ?: "Mafruza",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF3F3F3), CircleShape)
                    ) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    placeholder = { Text("Search places, tours...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    trailingIcon = { 
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
                            }
                        } else {
                            Icon(Icons.Outlined.Mic, contentDescription = null, tint = Color.Gray)
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF3F3F3),
                        unfocusedContainerColor = Color(0xFFF3F3F3),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )
            }

            // Banner (Featured Region)
            item {
                val featuredRegion = regions.find { it.nameEn == "Samarkand" } ?: regions.firstOrNull()
                featuredRegion?.let { region ->
                    Box(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onNavigateToDetail("region_${region.id}") }
                    ) {
                        AsyncImage(
                            model = UrlUtils.getFullUrl(region.coverUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                                        startY = 200f
                                    )
                                )
                        )
                        Text(
                            text = region.nameEn,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 110.dp, top = 20.dp)
                        )
                        
                        // Horizontal dots for banner
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(5) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (index == 1) Color.White else Color.White.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }

            // Regions (Suggestions)
            item {
                SectionHeader("Suggestions", onSeeAll = {})
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(regions) { region ->
                        RegionItem(region) { onNavigateToDetail("region_${region.id}") }
                    }
                }
            }

            // Properties (Places)
            item {
                SectionHeader("Top Destinations", onSeeAll = {})
            }

            val chunkedProperties = properties.chunked(2)
            items(chunkedProperties) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pair.forEach { property ->
                        Box(modifier = Modifier.weight(1f)) {
                            PropertyCard(
                                property = property,
                                isFavorite = property.id in favoritePropertyIds,
                                onFavoriteClick = { viewModel.toggleFavorite(property.id, "property") },
                                onClick = { onNavigateToDetail("property_${property.id}") }
                            )
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tour Packages
            if (tours.isNotEmpty()) {
                item {
                    SectionHeader("Tour Packages", onSeeAll = onSeeAllPackages)
                }
                items(tours) { tour ->
                    TourPackageItem(
                        tour = tour,
                        isFavorite = tour.id in favoriteTourIds,
                        onFavoriteClick = { viewModel.toggleFavorite(tour.id, "tour") },
                        onClick = { onNavigateToDetail("tour_${tour.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "See all",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.clickable { onSeeAll() }
        )
    }
}

@Composable
fun RegionItem(region: Region, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3F3F3)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = UrlUtils.getFullUrl(region.iconUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            region.nameEn, 
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PropertyCard(
    property: Property,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Box {
            AsyncImage(
                model = UrlUtils.getFullUrl(property.coverUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
                error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
            )
            
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB400), modifier = Modifier.size(14.dp))
            }
        }
        
        Text(
            text = property.nameEn,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = property.address?.split(",")?.lastOrNull()?.trim() ?: "Uzbekistan",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TourPackageItem(
    tour: Tour,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AsyncImage(
                    model = UrlUtils.getFullUrl(tour.coverUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    error = androidx.compose.ui.res.painterResource(com.example.guidemetestapp.R.drawable.placeholder_image)
                )
                
                IconButton(
                    onClick = { onFavoriteClick() },
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .background(Color.White.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(tour.nameEn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(tour.descriptionEn ?: "", color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${tour.durationDays} Days • ${tour.transportType ?: "Public"}", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Text("$${tour.avgTotalCost?.toInt() ?: 0}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF6B5E71))
        }
    }
}
