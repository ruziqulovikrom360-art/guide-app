package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.guidemetestapp.data.api.UrlUtils
import com.example.guidemetestapp.presentation.planner.*
import com.example.guidemetestapp.presentation.resort.*
import com.example.guidemetestapp.ui.components.GlassyCard
import com.example.guidemetestapp.ui.components.PremiumButton
import com.example.guidemetestapp.ui.components.SearchBarPremium
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

sealed class PlannerScreenState {
    object Selection : PlannerScreenState()
    object Details : PlannerScreenState()
    object MapList : PlannerScreenState()
    object FullMap : PlannerScreenState()
    object MapInfo : PlannerScreenState()
}

@Composable
fun PlannerScreen(
    plannerViewModel: PlannerViewModel = hiltViewModel(),
    resortViewModel: ResortViewModel = hiltViewModel(),
    onNavigateToTourMap: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    var screenState by remember { mutableStateOf<PlannerScreenState>(PlannerScreenState.Selection) }
    val plannerState by plannerViewModel.collectAsState()
    val resortState by resortViewModel.collectAsState()

    plannerViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PlannerSideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_LONG).show()
            }
            PlannerSideEffect.StartTour -> {
                val tourId = plannerState.selectedTour?.id ?: 0
                onNavigateToTourMap(tourId)
            }
            else -> {}
        }
    }

    resortViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ResortSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    when (screenState) {
        PlannerScreenState.Selection -> PlannerSelectionScreen(
            state = plannerState,
            onTourSelected = { tour ->
                plannerViewModel.onTourSelected(tour)
                screenState = PlannerScreenState.Details
            }
        )
        PlannerScreenState.Details -> PlannerDetailsScreen(
            state = plannerState,
            onBack = { screenState = PlannerScreenState.Selection },
            onStart = {
                val tourId = plannerState.selectedTour?.id ?: 0
                onNavigateToTourMap(tourId)
            },
            onVehicleChange = plannerViewModel::onVehicleTypeChanged,
            onDurationChange = plannerViewModel::onDurationChanged,
            onPromoCodeChange = plannerViewModel::onPromoCodeChanged,
            onResortClick = { screenState = PlannerScreenState.MapList }
        )
        PlannerScreenState.MapList -> PlannerMapListScreen(
            state = resortState,
            onBack = { screenState = PlannerScreenState.Details },
            onItemClick = { screenState = PlannerScreenState.FullMap },
            onSearchQueryChanged = resortViewModel::onSearchQueryChanged,
            onToggleSort = resortViewModel::toggleSortOrder
        )
        PlannerScreenState.FullMap -> PlannerFullMapScreen(
            onClose = { screenState = PlannerScreenState.MapInfo }
        )
        PlannerScreenState.MapInfo -> PlannerMapInfoScreen(
            onBack = { screenState = PlannerScreenState.MapList }
        )
    }
}

@Composable
fun PlannerSelectionScreen(
    state: PlannerState,
    onTourSelected: (com.example.guidemetestapp.data.model.Tour) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Text(
                    "First, choose one from\ntour packages!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                state.availableTours.forEach { tour ->
                    val imageUrl = UrlUtils.getFullUrl(tour.coverUrl)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(bottom = 20.dp)
                            .shadow(8.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onTourSelected(tour) }
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.8f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(20.dp)
                        ) {
                            Text(
                                tour.nameEn,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Text(
                                "${tour.durationDays} days • From $${tour.avgTotalCost ?: "---"}",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DestinationSmallCard(name: String, imageUrl: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(150.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Text(
                name,
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerDetailsScreen(
    state: PlannerState,
    onBack: () -> Unit,
    onStart: () -> Unit,
    onVehicleChange: (VehicleType) -> Unit,
    onDurationChange: (Int) -> Unit,
    onPromoCodeChange: (String) -> Unit,
    onResortClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.selectedPackage, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Trip Configuration", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                PlannerDetailItem("VEHICLE", state.vehicleType.displayName, ">") {
                    val nextVehicle = if (state.vehicleType == VehicleType.PUBLIC) VehicleType.PRIVATE else VehicleType.PUBLIC
                    onVehicleChange(nextVehicle)
                }
                
                PlannerDetailItem("DURATION", "${state.durationDays} days", ">") {
                    val nextDuration = when(state.durationDays) {
                        7 -> 10
                        10 -> 3
                        else -> 7
                    }
                    onDurationChange(nextDuration)
                }
                
                PlannerDetailItem("PAYMENT", state.paymentMethod, ">") {}
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Promo Code", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            SearchBarPremium(
                value = state.promoCode,
                onValueChange = onPromoCodeChange,
                placeholder = "Enter promo code",
                trailingIcon = {
                    if (state.promoCode.isNotEmpty()) {
                        Text("Apply", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { })
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("ITINERARY PLACES", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            state.selectedTour?.stops?.forEachIndexed { index, stop ->
                PlaceItem(
                    title = "Stop ${index + 1}",
                    description = stop.noteEn ?: "",
                    price = "$--",
                    imageUrl = "https://picsum.photos/seed/${stop.id}/100/100",
                    onClick = onResortClick
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (state.selectedTour == null || state.selectedTour.stops.isEmpty()) {
                PlaceItem(
                    title = "Magic City",
                    description = "Fulfilled with joy • 1 day",
                    price = "$20",
                    imageUrl = "https://picsum.photos/seed/magic/100/100"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PlaceItem(
                    title = "Amirsoy Resort",
                    description = "Great view of nature • 3 days",
                    price = "$180",
                    imageUrl = "https://picsum.photos/seed/amirsoy/100/100",
                    onClick = onResortClick
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF8F8F8),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SummaryRow("Places Total", "$${state.placesPrice}")
                    SummaryRow("Vehicle Fee", "$${state.vehicleType.basePrice}")
                    SummaryRow("Service Fee", "$${state.serviceFee}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                    SummaryRow("Grand Total", "$${String.format("%.2f", state.total)}", isTotal = true)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            PremiumButton(
                text = "Confirm and Start",
                onClick = onStart
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerMapListScreen(
    state: ResortState,
    onBack: () -> Unit,
    onItemClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onToggleSort: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "https://picsum.photos/seed/mafruza/100/100",
                            contentDescription = null,
                            modifier = Modifier.size(40.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Mafruza", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Simulated Map background
            AsyncImage(
                model = "https://picsum.photos/seed/map/800/1200",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                    IconButton(onClick = onBack, modifier = Modifier.background(Color.White, CircleShape)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = { Icon(Icons.Default.FilterList, null) },
                    placeholder = { Text("Search Amirsoy") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                Text("March 20 - 23  • 3 room • 5 guests", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))

                Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = false, onClick = {}, label = { Text("Filter") })
                    FilterChip(
                        selected = true, 
                        onClick = onToggleSort, 
                        label = { Text(if (state.sortByPriceAsc) "Price: Low to High" else "Price: High to Low") }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text("${state.filteredResorts.size} range", fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterVertically))
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bottom list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    state.filteredResorts.forEach { resort ->
                        val imageUrl = UrlUtils.getFullUrl(resort.imageUrl)
                        PlaceListItem(resort.name, "${resort.rooms} rooms & ${resort.description.take(20)}...", "$${resort.pricePerNight}", imageUrl, onItemClick)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PlannerDetailItem(label: String, value: String, trailing: String, onClick: () -> Unit = {}) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, modifier = Modifier.width(100.dp), color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(value, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
            Text(trailing, color = Color.Gray)
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
    }
}


@Composable
fun PlaceItem(title: String, description: String, price: String, imageUrl: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(description, color = Color.Gray, fontSize = 12.sp)
        }
        Text(price, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = if (isTotal) Color.Black else Color.Gray, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PlannerFullMapScreen(onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = "https://picsum.photos/seed/fullmap/1000/1000",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onClose,
            modifier = Modifier.padding(16.dp).background(Color.White, CircleShape)
        ) {
            Icon(Icons.Default.Close, null)
        }
    }
}

@Composable
fun PlannerMapInfoScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AsyncImage(
            model = "https://picsum.photos/seed/fullmap/1000/1000",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Amirsoy Resort", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Bostanlyk district, Uzbekistan", color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Back to list")
                }
            }
        }
    }
}

@Composable
fun PlaceListItem(title: String, description: String, price: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(description, color = Color.Gray, fontSize = 12.sp)
            }
            Text(price, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

