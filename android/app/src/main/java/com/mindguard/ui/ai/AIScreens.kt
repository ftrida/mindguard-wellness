package com.mindguard.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
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
class AIViewModel @Inject constructor(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _twin = MutableStateFlow<NetworkResult<DigitalTwinResponse>?>(null)
    val twin: StateFlow<NetworkResult<DigitalTwinResponse>?> = _twin

    private val _drift = MutableStateFlow<NetworkResult<BehaviorDriftResponse>?>(null)
    val drift: StateFlow<NetworkResult<BehaviorDriftResponse>?> = _drift

    private val _stress = MutableStateFlow<NetworkResult<StressLikelihoodResponse>?>(null)
    val stress: StateFlow<NetworkResult<StressLikelihoodResponse>?> = _stress

    private val _chatResponse = MutableStateFlow<NetworkResult<CoachChatResponse>?>(null)
    val chatResponse: StateFlow<NetworkResult<CoachChatResponse>?> = _chatResponse

    private val _recs = MutableStateFlow<NetworkResult<List<RecommendationResponse>>?>(null)
    val recs: StateFlow<NetworkResult<List<RecommendationResponse>>?> = _recs

    fun loadTwin() { viewModelScope.launch { aiRepository.getTwin().collect { _twin.value = it } } }
    fun loadDrift() { viewModelScope.launch { aiRepository.getBehaviorDrift().collect { _drift.value = it } } }
    fun loadStress() { viewModelScope.launch { aiRepository.getStressAssessment().collect { _stress.value = it } } }
    fun loadRecs() { viewModelScope.launch { aiRepository.getRecommendations().collect { _recs.value = it } } }

    fun sendMessage(msg: String) {
        viewModelScope.launch {
            aiRepository.sendCoachMessage(msg).collect { _chatResponse.value = it }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalLifestyleTwinScreen(navController: NavController, viewModel: AIViewModel = hiltViewModel()) {
    val twinState by viewModel.twin.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadTwin() }

    Scaffold(topBar = { TopAppBar(title = { Text("Digital Lifestyle Twin") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            val data = (twinState as? NetworkResult.Success)?.data
            Card(modifier = Modifier.fillMaxWidth().height(160.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Baseline Snapshot", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Sleep Baseline: ${data?.sleepBaseline ?: 7.5f} hrs", fontSize = 16.sp)
                    Text("Screen Time Baseline: ${data?.screenTimeBaseline ?: 4.0f} hrs", fontSize = 16.sp)
                    Text("Mood Baseline: ${data?.moodBaseline ?: 7.0f} / 10", fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BehaviorAnalysisScreen(navController: NavController, viewModel: AIViewModel = hiltViewModel()) {
    val driftState by viewModel.drift.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadDrift() }

    Scaffold(topBar = { TopAppBar(title = { Text("Behavior Drift Engine") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            val data = (driftState as? NetworkResult.Success)?.data
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Drift Score: ${data?.driftScore ?: 0.0f}%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Consistency: ${data?.consistencyScore ?: 100.0f}%", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(data?.explanation ?: "Habit consistency is aligned with target baseline.", fontSize = 14.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StressLikelihoodScreen(navController: NavController, viewModel: AIViewModel = hiltViewModel()) {
    val stressState by viewModel.stress.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadStress() }

    Scaffold(topBar = { TopAppBar(title = { Text("Stress Likelihood Engine") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            val data = (stressState as? NetworkResult.Success)?.data
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Estimated Stress Score", fontSize = 16.sp)
                    Text("${data?.stressScore?.toInt() ?: 15} / 100", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    Text("Confidence Score: ${data?.confidenceScore?.toInt() ?: 90}%", fontSize = 14.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AICoachChatScreen(navController: NavController, viewModel: AIViewModel = hiltViewModel()) {
    var textMessage by remember { mutableStateOf("") }
    val chatState by viewModel.chatResponse.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("AI Wellness Coach") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            val memory = (chatState as? NetworkResult.Success)?.data?.memory ?: emptyList()
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(memory) { msg ->
                    val isUser = msg.role == "user"
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(msg.content, modifier = Modifier.padding(12.dp), color = if (isUser) Color.White else Color.Black)
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = textMessage,
                    onValueChange = { textMessage = it },
                    placeholder = { Text("Ask your AI Coach...") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    if (textMessage.isNotBlank()) {
                        viewModel.sendMessage(textMessage)
                        textMessage = ""
                    }
                }) { Icon(Icons.Default.Send, contentDescription = "Send") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(navController: NavController, viewModel: AIViewModel = hiltViewModel()) {
    val recsState by viewModel.recs.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadRecs() }

    Scaffold(topBar = { TopAppBar(title = { Text("AI Recommendations") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        val list = (recsState as? NetworkResult.Success)?.data ?: emptyList()
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(list) { rec ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(rec.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(rec.description, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
