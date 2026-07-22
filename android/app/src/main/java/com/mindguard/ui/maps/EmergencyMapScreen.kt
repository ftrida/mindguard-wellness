package com.mindguard.ui.maps

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyMapScreen(navController: NavController) {
    val defaultLocation = LatLng(37.7749, -122.4194) // San Francisco / User Location placeholder
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency SOS & GPS Sharing") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = defaultLocation),
                    title = "Your Live Location",
                    snippet = "Sharing enabled for Emergency Contacts"
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Broadcast Emergency Alert", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pressing below sends live GPS coordinates to your registered emergency contacts.", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("SEND SOS NOW", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
