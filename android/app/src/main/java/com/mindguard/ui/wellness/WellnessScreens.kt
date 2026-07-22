package com.mindguard.ui.wellness

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
import androidx.compose.ui.text.font.FontWeight
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WellnessViewModel @Inject constructor(
    private val wellnessRepository: WellnessRepository
) : ViewModel() {

    private val _logResult = MutableStateFlow<NetworkResult<Any>?>(null)
    val logResult: StateFlow<NetworkResult<Any>?> = _logResult

    private val _contacts = MutableStateFlow<NetworkResult<List<EmergencyContactResponse>>?>(null)
    val contacts: StateFlow<NetworkResult<List<EmergencyContactResponse>>?> = _contacts

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

    fun createJournal(title: String, content: String) {
        viewModelScope.launch {
            wellnessRepository.createJournal(JournalCreate(title = title, content = content)).collect {
                _logResult.value = it
            }
        }
    }

    fun loadEmergencyContacts() {
        viewModelScope.launch {
            wellnessRepository.getEmergencyContacts().collect { _contacts.value = it }
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

    Scaffold(topBar = { TopAppBar(title = { Text("Log Daily Lifestyle") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(value = sleepText, onValueChange = { sleepText = it }, label = { Text("Sleep Hours") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = screenText, onValueChange = { screenText = it }, label = { Text("Screen Time Hours") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = activeText, onValueChange = { activeText = it }, label = { Text("Active Minutes") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.logLifestyle(sleepText.toFloatOrNull() ?: 7.5f, screenText.toFloatOrNull() ?: 4.0f, activeText.toIntOrNull() ?: 30) },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Save Lifestyle Log") }

            if (result is NetworkResult.Success) {
                Text("Log saved successfully!", color = MaterialTheme.colorScheme.primary)
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

    Scaffold(topBar = { TopAppBar(title = { Text("Track Mood") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Mood Rating: $moodScore / 10", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Slider(value = moodScore.toFloat(), onValueChange = { moodScore = it.toInt() }, valueRange = 1f..10f, steps = 8)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes / Reflections") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { viewModel.logMood(moodScore, notes) }, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Save Mood Entry") }
            if (result is NetworkResult.Success) Text("Mood logged successfully!", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Mindfulness Journal") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Write your thoughts...") }, modifier = Modifier.fillMaxWidth().height(200.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { viewModel.createJournal(title, content) }, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Save Journal Entry") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Guided Meditation") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("10-Minute Mindfulness Breathing", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {}) { Text("Start Session") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSessionScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Focus Pomodoro Timer") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("25:00", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {}) { Text("Start Focus Clock") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    val contactsState by viewModel.contacts.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadEmergencyContacts() }

    Scaffold(topBar = { TopAppBar(title = { Text("Emergency Contacts") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            val contactsList = (contactsState as? NetworkResult.Success)?.data ?: emptyList()
            LazyColumn {
                items(contactsList) { c ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(c.name, fontWeight = FontWeight.Bold)
                            Text("${c.relationship} • ${c.phoneNumber}", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
