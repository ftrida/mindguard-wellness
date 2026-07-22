package com.mindguard.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mindguard.core.navigation.Screen
import com.mindguard.core.network.NetworkResult
import com.mindguard.data.local.datastore.TokenManager
import com.mindguard.data.remote.dto.LoginRequest
import com.mindguard.data.remote.dto.RegisterRequest
import com.mindguard.data.repository.AuthRepository
import com.mindguard.data.repository.WellnessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.mindguard.data.remote.dto.ProfileRequest
import com.mindguard.data.remote.dto.ProfileResponse
import com.mindguard.data.remote.dto.UserResponse
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val wellnessRepository: WellnessRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResult<Any>?>(null)
    val loginState: StateFlow<NetworkResult<Any>?> = _loginState

    private val _registerState = MutableStateFlow<NetworkResult<Any>?>(null)
    val registerState: StateFlow<NetworkResult<Any>?> = _registerState

    private val _profileState = MutableStateFlow<NetworkResult<ProfileResponse>?>(null)
    val profileState: StateFlow<NetworkResult<ProfileResponse>?> = _profileState

    private val _updateProfileState = MutableStateFlow<NetworkResult<ProfileResponse>?>(null)
    val updateProfileState: StateFlow<NetworkResult<ProfileResponse>?> = _updateProfileState

    fun login(emailOrUsername: String, pass: String) {
        viewModelScope.launch {
            authRepository.login(LoginRequest(emailOrUsername, pass)).collect {
                _loginState.value = it
            }
        }
    }

    fun register(email: String, username: String, pass: String, fullName: String) {
        viewModelScope.launch {
            authRepository.register(RegisterRequest(email, username, pass, fullName)).collect {
                _registerState.value = it
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            wellnessRepository.getProfile().collect {
                _profileState.value = it
            }
        }
    }

    fun updateProfile(request: ProfileRequest) {
        viewModelScope.launch {
            wellnessRepository.updateProfile(request).collect {
                _updateProfileState.value = it
                if (it is NetworkResult.Success) {
                    _profileState.value = it
                }
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            authRepository.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController, tokenManager: TokenManager = hiltViewModel<SplashViewModel>().tokenManager) {
    LaunchedEffect(Unit) {
        delay(1500)
        val token = tokenManager.getAccessToken()
        if (!token.isNullOrEmpty()) {
            navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
        } else {
            navController.navigate(Screen.Login.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("MindGuard AI", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Enterprise Security & Wellness Platform", fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
        }
    }
}

@HiltViewModel
class SplashViewModel @Inject constructor(val tokenManager: TokenManager) : ViewModel()

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is NetworkResult.Success) {
            navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Sign in to your MindGuard account", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email or Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (loginState is NetworkResult.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("Don't have an account? Register")
        }

        if (loginState is NetworkResult.Error) {
            Text((loginState as NetworkResult.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        if (registerState is NetworkResult.Success) {
            navController.navigate(Screen.Login.route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.register(email, username, password, fullName) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (registerState is NetworkResult.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Register", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("Already have an account? Login")
        }

        if (registerState is NetworkResult.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text((registerState as NetworkResult.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val profileState by viewModel.profileState.collectAsState()
    val updateState by viewModel.updateProfileState.collectAsState()
    
    var isEditing by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var timezone by remember { mutableStateOf("UTC") }
    var country by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("en") }
    var occupation by remember { mutableStateOf("") }
    var emergencyPreferences by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(profileState) {
        if (profileState is NetworkResult.Success) {
            val data = (profileState as NetworkResult.Success<ProfileResponse>).data
            gender = data.gender ?: ""
            dob = data.dob ?: ""
            height = data.height?.toString() ?: ""
            weight = data.weight?.toString() ?: ""
            timezone = data.timezone
            country = data.country ?: ""
            language = data.language
            occupation = data.occupation ?: ""
            emergencyPreferences = data.emergencyPreferences ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Management", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isEditing) {
                            val req = ProfileRequest(
                                dob = dob.ifEmpty { null },
                                gender = gender.ifEmpty { null },
                                height = height.toFloatOrNull(),
                                weight = weight.toFloatOrNull(),
                                timezone = timezone,
                                country = country.ifEmpty { null },
                                language = language,
                                occupation = occupation.ifEmpty { null },
                                emergencyPreferences = emergencyPreferences.ifEmpty { null }
                            )
                            viewModel.updateProfile(req)
                            isEditing = false
                        } else {
                            isEditing = true
                        }
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Save" else "Edit"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (profileState) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NetworkResult.Error -> {
                    Text(
                        text = (profileState as NetworkResult.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // User Profile circular avatar
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(50.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "MG",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isEditing) {
                            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = occupation, onValueChange = { occupation = it }, label = { Text("Occupation") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = emergencyPreferences, onValueChange = { emergencyPreferences = it }, label = { Text("Emergency Preferences") }, modifier = Modifier.fillMaxWidth())
                        } else {
                            ProfileDetailRow(label = "Gender", value = gender.ifEmpty { "Not specified" })
                            ProfileDetailRow(label = "Date of Birth", value = dob.ifEmpty { "Not specified" })
                            ProfileDetailRow(label = "Height", value = if (height.isNotEmpty()) "$height cm" else "Not specified")
                            ProfileDetailRow(label = "Weight", value = if (weight.isNotEmpty()) "$weight kg" else "Not specified")
                            ProfileDetailRow(label = "Country", value = country.ifEmpty { "Not specified" })
                            ProfileDetailRow(label = "Occupation", value = occupation.ifEmpty { "Not specified" })
                            ProfileDetailRow(label = "Language", value = language)
                            ProfileDetailRow(label = "Timezone", value = timezone)
                            ProfileDetailRow(label = "Emergency Prefs", value = emergencyPreferences.ifEmpty { "None" })
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.logout(navController) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Log Out", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        if (updateState is NetworkResult.Success) {
                            Text("Profile updated successfully!", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}
