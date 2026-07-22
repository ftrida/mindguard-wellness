package com.mindguard.ui.gamification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mindguard.core.network.NetworkResult
import com.mindguard.data.remote.dto.*
import com.mindguard.data.repository.AIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamificationViewModel @Inject constructor(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _goals = MutableStateFlow<NetworkResult<List<GoalResponse>>?>(null)
    val goals: StateFlow<NetworkResult<List<GoalResponse>>?> = _goals

    private val _achievements = MutableStateFlow<NetworkResult<List<AchievementResponse>>?>(null)
    val achievements: StateFlow<NetworkResult<List<AchievementResponse>>?> = _achievements

    private val _streak = MutableStateFlow<NetworkResult<StreakResponse>?>(null)
    val streak: StateFlow<NetworkResult<StreakResponse>?> = _streak

    fun loadData() {
        viewModelScope.launch { aiRepository.getGoals().collect { _goals.value = it } }
        viewModelScope.launch { aiRepository.getAchievements().collect { _achievements.value = it } }
        viewModelScope.launch { aiRepository.getStreak().collect { _streak.value = it } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(navController: NavController, viewModel: GamificationViewModel = hiltViewModel()) {
    val goalsState by viewModel.goals.collectAsState()
    val streakState by viewModel.streak.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadData() }

    Scaffold(topBar = { TopAppBar(title = { Text("Goals & Milestones") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            val streak = (streakState as? NetworkResult.Success)?.data?.currentStreak ?: 3
            Card(modifier = Modifier.fillMaxWidth().height(100.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🔥 Current Log Streak", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("$streak Days", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val goalsList = (goalsState as? NetworkResult.Success)?.data ?: emptyList()
            LazyColumn {
                items(goalsList) { g ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(g.title, fontWeight = FontWeight.Bold)
                            LinearProgressIndicator(progress = (g.currentValue / g.targetValue).coerceIn(0f, 1f), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                            Text("Progress: ${g.currentValue} / ${g.targetValue}", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(navController: NavController, viewModel: GamificationViewModel = hiltViewModel()) {
    val achievementsState by viewModel.achievements.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadData() }

    Scaffold(topBar = { TopAppBar(title = { Text("Unlocked Badges") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        val list = (achievementsState as? NetworkResult.Success)?.data ?: emptyList()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(list) { badge ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🏆", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(badge.title, fontWeight = FontWeight.Bold)
                            Text(badge.description, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Wellness Reports") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Aggregate Report", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Summary of sleep, mood, and stress likelihood", fontSize = 14.sp, color = Color.Gray)
                }
            }
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Weekly Analytical Report", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Includes habit drift indices and coach notes", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}
