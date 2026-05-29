package com.example.guidemetestapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResortSelectionScreen(
    onBack: () -> Unit,
    onResortSelected: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Resort (Filter/Sort)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val resorts = listOf("Amirsoy Resort 1", "Amirsoy Resort 2", "Amirsoy Resort 3", "Amirsoy Resort 4", "Amirsoy Resort 5")
            items(resorts.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onResortSelected(resorts[index]) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = resorts[index], style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Price per night: $${30 + index * 5}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
