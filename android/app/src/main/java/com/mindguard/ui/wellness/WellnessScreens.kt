package com.mindguard.ui.wellness

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mindguard.core.network.NetworkResult
import com.mindguard.data.remote.dto.*
import com.mindguard.data.repository.WellnessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WellnessViewModel @Inject constructor(
    private val wellnessRepository: WellnessRepository
) : ViewModel() {

    private val _logResult = MutableStateFlow<NetworkResult<Any>?>(null)
    val logResult: StateFlow<NetworkResult<Any>?> = _logResult

    private val _journals = MutableStateFlow<NetworkResult<List<JournalResponse>>?>(null)
    val journals: StateFlow<NetworkResult<List<JournalResponse>>?> = _journals

    fun clearResult() {
        _logResult.value = null
    }

    fun logLifestyle(sleep: Float, screen: Float, active: Int) {
        viewModelScope.launch {
            wellnessRepository.logLifestyle(DailyLifestyleCreate(sleepHours = sleep, screenTimeHours = screen, activeMinutes = active)).collect {
                _logResult.value = it
            }
        }
    }

    fun logMood(score: Int, notes: String) {
        viewModelScope.launch {
            wellnessRepository.logMood(MoodCreate(moodScore = score, notes = notes)).collect {
                _logResult.value = it
            }
        }
    }

    fun loadJournals() {
        viewModelScope.launch {
            wellnessRepository.getJournals().collect {
                _journals.value = it
            }
        }
    }

    fun createJournal(title: String, content: String) {
        viewModelScope.launch {
            wellnessRepository.createJournal(JournalCreate(title = title, content = content)).collect {
                _logResult.value = it
                if (it is NetworkResult.Success) {
                    loadJournals()
                }
            }
        }
    }

    fun logMeditation(durationSeconds: Int) {
        viewModelScope.launch {
            wellnessRepository.logMeditation(durationSeconds).collect {
                _logResult.value = it
            }
        }
    }

    fun logFocus(durationSeconds: Int) {
        viewModelScope.launch {
            wellnessRepository.logFocus(durationSeconds).collect {
                _logResult.value = it
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifestyleTrackerScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var sleepText by remember { mutableStateOf("7.5") }
    var screenText by remember { mutableStateOf("4.0") }
    var activeText by remember { mutableStateOf("30") }
    val result by viewModel.logResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearResult()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Daily Lifestyle", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = sleepText,
                onValueChange = { sleepText = it },
                label = { Text("Sleep Hours") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = screenText,
                onValueChange = { screenText = it },
                label = { Text("Screen Time Hours") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = activeText,
                onValueChange = { activeText = it },
                label = { Text("Active Minutes") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val sleep = sleepText.toFloatOrNull() ?: 7.5f
                    val screen = screenText.toFloatOrNull() ?: 4.0f
                    val active = activeText.toIntOrNull() ?: 30
                    viewModel.logLifestyle(sleep, screen, active)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = result !is NetworkResult.Loading
            ) {
                if (result is NetworkResult.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Lifestyle Log", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (result is NetworkResult.Success) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = MaterialTheme.colorScheme.primary)
                        Text("Lifestyle metrics saved successfully!", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            if (result is NetworkResult.Error) {
                Text(
                    text = (result as NetworkResult.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTrackerScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var moodScore by remember { mutableStateOf(5) }
    var notes by remember { mutableStateOf("") }
    val result by viewModel.logResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearResult()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Mood", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("How are you feeling today?", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Text("Mood Rating: $moodScore / 10", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)

            Slider(
                value = moodScore.toFloat(),
                onValueChange = { moodScore = it.toInt() },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes / Reflections") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Button(
                onClick = { viewModel.logMood(moodScore, notes) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = result !is NetworkResult.Loading
            ) {
                if (result is NetworkResult.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Mood Entry", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (result is NetworkResult.Success) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = MaterialTheme.colorScheme.primary)
                        Text("Mood logged successfully!", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            if (result is NetworkResult.Error) {
                Text((result as NetworkResult.Error).message, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val result by viewModel.logResult.collectAsState()
    val journalsState by viewModel.journals.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearResult()
        viewModel.loadJournals()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mindfulness Journal", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Journal Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Write your thoughts...") }, modifier = Modifier.fillMaxWidth().height(120.dp))

            Button(
                onClick = {
                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        viewModel.createJournal(title, content)
                        title = ""
                        content = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = result !is NetworkResult.Loading
            ) {
                if (result is NetworkResult.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Journal Entry", fontWeight = FontWeight.Bold)
                }
            }

            Text("Journal History", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                when (journalsState) {
                    is NetworkResult.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is NetworkResult.Success -> {
                        val list = (journalsState as NetworkResult.Success<List<JournalResponse>>).data
                        if (list.isEmpty()) {
                            Text("No journal entries yet. Start writing!", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(list) { j ->
                                    Card(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(j.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                Text(
                                                    text = "Sentiment: ${String.format(Locale.US, "%.2f", j.sentimentScore ?: 0.0f)}",
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(j.content, fontSize = 14.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        Text("Failed to load history", modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
                    }
                    else -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var secondsLeft by remember { mutableStateOf(600) } // 10 minutes default
    var isTimerActive by remember { mutableStateOf(false) }
    val result by viewModel.logResult.collectAsState()

    LaunchedEffect(isTimerActive, secondsLeft) {
        if (isTimerActive && secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        } else if (secondsLeft == 0 && isTimerActive) {
            isTimerActive = false
            viewModel.logMeditation(600)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guided Meditation", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("10-Minute Mindfulness Breathing", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                val mins = secondsLeft / 60
                val secs = secondsLeft % 60
                Text(
                    text = String.format(Locale.US, "%02d:%02d", mins, secs),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { isTimerActive = !isTimerActive }) {
                        Text(if (isTimerActive) "Pause" else "Start Session")
                    }
                    OutlinedButton(onClick = {
                        isTimerActive = false
                        secondsLeft = 600
                    }) {
                        Text("Reset")
                    }
                }

                if (result is NetworkResult.Success) {
                    Text("Meditation logged successfully!", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSessionScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var secondsLeft by remember { mutableStateOf(1500) } // 25 minutes default
    var isTimerActive by remember { mutableStateOf(false) }
    val result by viewModel.logResult.collectAsState()

    LaunchedEffect(isTimerActive, secondsLeft) {
        if (isTimerActive && secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        } else if (secondsLeft == 0 && isTimerActive) {
            isTimerActive = false
            viewModel.logFocus(1500)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Pomodoro Timer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("25-Minute Work Interval", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                val mins = secondsLeft / 60
                val secs = secondsLeft % 60
                Text(
                    text = String.format(Locale.US, "%02d:%02d", mins, secs),
                    fontSize = 54.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { isTimerActive = !isTimerActive }) {
                        Text(if (isTimerActive) "Pause" else "Start Focus Clock")
                    }
                    OutlinedButton(onClick = {
                        isTimerActive = false
                        secondsLeft = 1500
                    }) {
                        Text("Reset")
                    }
                }

                if (result is NetworkResult.Success) {
                    Text("Focus session logged successfully!", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
