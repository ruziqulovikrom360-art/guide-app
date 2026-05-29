package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.example.guidemetestapp.data.api.UrlUtils
import com.example.guidemetestapp.data.model.GuideItem
import com.example.guidemetestapp.ui.components.FullScreenLoading
import com.example.guidemetestapp.ui.viewmodel.GuideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: GuideViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val favoriteItems by viewModel.favoriteGuides.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    androidx.compose.runtime.LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Places", fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color.White)) {
            if (favoriteItems.isEmpty() && !isLoading) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(favoriteItems, key = { it.id }) { item ->
                        GuideCard(
                            item = item,
                            isFavorite = true,
                            onFavoriteClick = { 
                                viewModel.toggleFavorite(
                                    item.id, 
                                    if (item.category == "Tour") "tour" else "property"
                                ) 
                            },
                            onClick = { 
                                val prefix = if (item.category == "Tour") "tour" else "property"
                                onNavigateToDetail("${prefix}_${item.id}") 
                            }
                        )
                    }
                }
            }
            
            if (isLoading) {
                FullScreenLoading()
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Your heart is empty", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Save places you want to visit and they\nwill appear here.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 32.dp),
            lineHeight = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun GuideCard(
    item: GuideItem,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = UrlUtils.getFullUrl(item.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        item.category.uppercase(), 
                        color = Color.White, 
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 17.sp, maxLines = 1)
                Text(item.location, color = Color.Gray, fontSize = 13.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star, 
                        null, 
                        tint = Color(0xFFFFB400), 
                        modifier = Modifier.size(16.dp)
                    )
                    Text(" ${item.rating}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.background(Color.White, CircleShape).size(36.dp)
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
