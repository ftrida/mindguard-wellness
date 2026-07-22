package com.mindguard.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mindguard.core.navigation.Screen
import com.mindguard.core.network.NetworkResult
import com.mindguard.data.remote.dto.DigitalTwinResponse
import com.mindguard.data.remote.dto.StressLikelihoodResponse
import com.mindguard.data.repository.AIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _twinState = MutableStateFlow<NetworkResult<DigitalTwinResponse>?>(null)
    val twinState: StateFlow<NetworkResult<DigitalTwinResponse>?> = _twinState

    private val _stressState = MutableStateFlow<NetworkResult<StressLikelihoodResponse>?>(null)
    val stressState: StateFlow<NetworkResult<StressLikelihoodResponse>?> = _stressState

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            aiRepository.getTwin().collect { _twinState.value = it }
        }
        viewModelScope.launch {
            aiRepository.getStressAssessment().collect { _stressState.value = it }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val twinState by viewModel.twinState.collectAsState()
    val stressState by viewModel.stressState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MindGuard AI", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
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
            // Welcome Greeting & Date
            Text(
                text = "Welcome Back, User",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Today is Thursday, July 23, 2026",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Wellness Gauge Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Personal Wellness Score", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        val score = (twinState as? NetworkResult.Success)?.data?.wellnessScore ?: 85.0f
                        Text("${score.toInt()}/100", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("Optimal Baseline Stability", fontSize = 11.sp, color = Color.Gray)
                    }
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Quick Modules", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                item { QuickModuleCard("Digital Twin", Icons.Default.AccountBox, MaterialTheme.colorScheme.primary) { navController.navigate(Screen.DigitalTwin.route) } }
                item { QuickModuleCard("Behavior Drift", Icons.Default.Timeline, MaterialTheme.colorScheme.secondary) { navController.navigate(Screen.BehaviorDrift.route) } }
                item { QuickModuleCard("Stress Engine", Icons.Default.Warning, MaterialTheme.colorScheme.error) { navController.navigate(Screen.StressLikelihood.route) } }
                item { QuickModuleCard("AI Coach Chat", Icons.Default.Chat, MaterialTheme.colorScheme.tertiary) { navController.navigate(Screen.CoachChat.route) } }
                item { QuickModuleCard("Lifestyle Logs", Icons.Default.Edit, Color(0xFF00A896)) { navController.navigate(Screen.Lifestyle.route) } }
                item { QuickModuleCard("Mood Tracker", Icons.Default.Face, Color(0xFFF4D35E)) { navController.navigate(Screen.Mood.route) } }
                item { QuickModuleCard("Journal Notes", Icons.Default.Book, Color(0xFFEE964B)) { navController.navigate(Screen.Journal.route) } }
                item { QuickModuleCard("Meditation", Icons.Default.SelfImprovement, Color(0xFF02C39A)) { navController.navigate(Screen.Meditation.route) } }
                item { QuickModuleCard("Focus Session", Icons.Default.Timer, Color(0xFF0D3B66)) { navController.navigate(Screen.Focus.route) } }
                item { QuickModuleCard("Goals & Streaks", Icons.Default.Star, Color(0xFF8E44AD)) { navController.navigate(Screen.Goals.route) } }
                item { QuickModuleCard("Reports & Export", Icons.Default.Assessment, Color(0xFF27AE60)) { navController.navigate(Screen.Reports.route) } }
            }
        }
    }
}

@Composable
fun QuickModuleCard(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = color)
        }
    }
}
