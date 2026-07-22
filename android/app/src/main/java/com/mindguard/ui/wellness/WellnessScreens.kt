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
import androidx.compose.ui.graphics.Brush
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
import java.text.SimpleDateFormat
import java.util.Date
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
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            wellnessRepository.logLifestyle(
                DailyLifestyleCreate(
                    logDate = dateStr,
                    sleepHours = sleep,
                    screenTime = screen,
                    exerciseMinutes = active
                )
            ).collect {
                _logResult.value = it
            }
        }
    }

    fun logMood(score: Int, notes: String, category: String) {
        viewModelScope.launch {
            wellnessRepository.logMood(MoodCreate(moodScore = score, notes = notes, category = category)).collect {
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

    fun createJournal(title: String, content: String, category: String) {
        viewModelScope.launch {
            wellnessRepository.createJournal(JournalCreate(title = title, content = content, category = category)).collect {
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

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Daily Lifestyle", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F2027))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Track your daily metrics to balance baseline stability indices.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = sleepText,
                    onValueChange = { sleepText = it },
                    label = { Text("Sleep Hours", color = Color.White.copy(alpha = 0.8f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = screenText,
                    onValueChange = { screenText = it },
                    label = { Text("Screen Time Hours", color = Color.White.copy(alpha = 0.8f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = activeText,
                    onValueChange = { activeText = it },
                    label = { Text("Active Exercise Minutes", color = Color.White.copy(alpha = 0.8f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val sleep = sleepText.toFloatOrNull() ?: 7.5f
                        val screen = screenText.toFloatOrNull() ?: 4.0f
                        val active = activeText.toIntOrNull() ?: 30
                        viewModel.logLifestyle(sleep, screen, active)
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = result !is NetworkResult.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (result is NetworkResult.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Save Daily Log", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (result is NetworkResult.Success) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = Color(0xFF00C853))
                            Text("Lifestyle metrics synced to backend!", fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }

                if (result is NetworkResult.Error) {
                    Text(
                        text = (result as NetworkResult.Error).message,
                        color = Color(0xFFFF5252),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTrackerScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var moodScore by remember { mutableStateOf(7) }
    var notes by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Calm") }
    val result by viewModel.logResult.collectAsState()

    val moodCategories = listOf("Calm", "Happy", "Energetic", "Sad", "Anxious", "Angry", "Tired", "Stressed")

    LaunchedEffect(Unit) {
        viewModel.clearResult()
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF141E30),
            Color(0xFF243B55)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Mood State", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF141E30))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Reflect on your emotional balance.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Text(
                    text = "Mood Balance Score: $moodScore / 10",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Slider(
                    value = moodScore.toFloat(),
                    onValueChange = { moodScore = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Select Primary Category",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Row/Flow of Mood Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        val chunks = moodCategories.chunked(4)
                        items(chunks) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { cat ->
                                    val isSelected = selectedCategory == cat
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedCategory = cat },
                                        label = { Text(cat) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = Color.White,
                                            labelColor = Color.White.copy(alpha = 0.7f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes & Reflections", color = Color.White.copy(alpha = 0.8f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )

                Button(
                    onClick = { viewModel.logMood(moodScore, notes, selectedCategory) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = result !is NetworkResult.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (result is NetworkResult.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Sync Mood Entry", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (result is NetworkResult.Success) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00C853))
                            Text("Mood saved successfully!", fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }

                if (result is NetworkResult.Error) {
                    Text(
                        text = (result as NetworkResult.Error).message,
                        color = Color(0xFFFF5252),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(navController: NavController, viewModel: WellnessViewModel = hiltViewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    val result by viewModel.logResult.collectAsState()
    val journalsState by viewModel.journals.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearResult()
        viewModel.loadJournals()
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D1B2A),
            Color(0xFF1B263B)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mindfulness Journal", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1B2A))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Journal Title", color = Color.White.copy(alpha = 0.8f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (e.g. Reflections, Goals)", color = Color.White.copy(alpha = 0.8f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Write your thoughts...", color = Color.White.copy(alpha = 0.8f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )

                Button(
                    onClick = {
                        if (title.isNotEmpty() && content.isNotEmpty()) {
                            viewModel.createJournal(title, content, category)
                            title = ""
                            content = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = result !is NetworkResult.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (result is NetworkResult.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Save Journal Entry", fontWeight = FontWeight.Bold)
                    }
                }

                Text("Journal History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 8.dp))

                Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    when (journalsState) {
                        is NetworkResult.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                        is NetworkResult.Success -> {
                            val list = (journalsState as NetworkResult.Success<List<JournalResponse>>).data
                            if (list.isEmpty()) {
                                Text("No journal entries yet. Start writing!", modifier = Modifier.align(Alignment.Center), color = Color.White.copy(alpha = 0.6f))
                            } else {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(list) { j ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Text(j.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                                                    Text(
                                                        text = "Sentiment: ${String.format(Locale.US, "%.2f", j.sentimentScore ?: 0.0f)}",
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(j.content, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Category: ${j.category}",
                                                    fontSize = 11.sp,
                                                    color = Color.White.copy(alpha = 0.5f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        is NetworkResult.Error -> {
                            Text(
                                text = (journalsState as NetworkResult.Error).message,
                                modifier = Modifier.align(Alignment.Center),
                                color = Color(0xFFFF5252),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        else -> {}
                    }
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

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guided Meditation", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F2027))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                Text("10-Minute Mindfulness Breathing", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                val mins = secondsLeft / 60
                val secs = secondsLeft % 60
                Text(
                    text = String.format(Locale.US, "%02d:%02d", mins, secs),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { isTimerActive = !isTimerActive },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isTimerActive) "Pause" else "Start Session")
                    }
                    OutlinedButton(
                        onClick = {
                            isTimerActive = false
                            secondsLeft = 600
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Reset")
                    }
                }

                if (result is NetworkResult.Success) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00C853))
                            Text("Meditation logged successfully!", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                if (result is NetworkResult.Error) {
                    Text(
                        text = (result as NetworkResult.Error).message,
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Medium
                    )
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

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF141E30),
            Color(0xFF243B55)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Pomodoro Timer", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF141E30))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                Text("25-Minute Work Interval", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                val mins = secondsLeft / 60
                val secs = secondsLeft % 60
                Text(
                    text = String.format(Locale.US, "%02d:%02d", mins, secs),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { isTimerActive = !isTimerActive },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isTimerActive) "Pause" else "Start Focus Clock")
                    }
                    OutlinedButton(
                        onClick = {
                            isTimerActive = false
                            secondsLeft = 1500
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Reset")
                    }
                }

                if (result is NetworkResult.Success) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00C853))
                            Text("Focus session logged successfully!", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                if (result is NetworkResult.Error) {
                    Text(
                        text = (result as NetworkResult.Error).message,
                        color = Color(0xFFFF5252),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
