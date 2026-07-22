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
            Text("Register", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("Already have an account? Login")
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("User Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.logout(navController) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}
