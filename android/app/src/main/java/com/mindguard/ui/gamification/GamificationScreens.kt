package com.mindguard.ui.gamification

import androidx.compose.foundation.clickable
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

    private val _dailyReport = MutableStateFlow<NetworkResult<Map<String, Any>>?>(null)
    val dailyReport: StateFlow<NetworkResult<Map<String, Any>>?> = _dailyReport

    private val _weeklyReport = MutableStateFlow<NetworkResult<Map<String, Any>>?>(null)
    val weeklyReport: StateFlow<NetworkResult<Map<String, Any>>?> = _weeklyReport

    fun loadData() {
        viewModelScope.launch { aiRepository.getGoals().collect { _goals.value = it } }
        viewModelScope.launch { aiRepository.getAchievements().collect { _achievements.value = it } }
        viewModelScope.launch { aiRepository.getStreak().collect { _streak.value = it } }
    }

    fun loadDailyReport() {
        viewModelScope.launch { aiRepository.getDailyReport().collect { _dailyReport.value = it } }
    }

    fun loadWeeklyReport() {
        viewModelScope.launch { aiRepository.getWeeklyReport().collect { _weeklyReport.value = it } }
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
fun ReportsScreen(navController: NavController, viewModel: GamificationViewModel = hiltViewModel()) {
    val dailyState by viewModel.dailyReport.collectAsState()
    val weeklyState by viewModel.weeklyReport.collectAsState()
    var activeReportType by remember { mutableStateOf<String?>(null) } // "daily", "weekly" or null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wellness Reports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.loadDailyReport()
                        activeReportType = "daily"
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Daily Aggregate Report", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    Text("Click to fetch today's aggregate log counts", fontSize = 13.sp, color = Color.Gray)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.loadWeeklyReport()
                        activeReportType = "weekly"
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Weekly Analytical Report", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary)
                    Text("Click to compile weekly habit consistency analysis", fontSize = 13.sp, color = Color.Gray)
                }
            }

            if (activeReportType != null) {
                Text(
                    text = if (activeReportType == "daily") "Daily Metrics Summary" else "Weekly Analysis Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )

                val activeState = if (activeReportType == "daily") dailyState else weeklyState

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        when (activeState) {
                            is NetworkResult.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            is NetworkResult.Success -> {
                                val reportMap = (activeState as NetworkResult.Success<Map<String, Any>>).data
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(reportMap.entries.toList()) { entry ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(entry.key, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                            Text(entry.value.toString(), fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
                                        }
                                        Divider()
                                    }
                                }
                            }
                            is NetworkResult.Error -> {
                                Text(
                                    text = (activeState as NetworkResult.Error).message,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            else -> {
                                Text("No data compiled", modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }
}
