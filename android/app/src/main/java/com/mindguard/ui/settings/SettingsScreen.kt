package com.mindguard.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var darkModeEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Settings") },
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
                .padding(16.dp)
        ) {
            Text("Preferences", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Dark Theme", fontSize = 16.sp)
                Switch(checked = darkModeEnabled, onCheckedChange = { darkModeEnabled = it })
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Wellness Notifications & Reminders", fontSize = 16.sp)
                Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Biometric Authentication", fontSize = 16.sp)
                Switch(checked = biometricEnabled, onCheckedChange = { biometricEnabled = it })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("About", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("MindGuard AI Client v1.0.0", fontSize = 14.sp)
            Text("Backend URL: https://mindguard-api-gz19.onrender.com", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}
