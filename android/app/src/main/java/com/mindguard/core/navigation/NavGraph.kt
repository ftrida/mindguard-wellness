package com.mindguard.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mindguard.ui.auth.*
import com.mindguard.ui.dashboard.DashboardScreen
import com.mindguard.ui.wellness.*
import com.mindguard.ui.ai.*
import com.mindguard.ui.gamification.*
import com.mindguard.ui.maps.EmergencyMapScreen
import com.mindguard.ui.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Lifestyle.route) { LifestyleTrackerScreen(navController) }
        composable(Screen.Mood.route) { MoodTrackerScreen(navController) }
        composable(Screen.Journal.route) { JournalScreen(navController) }
        composable(Screen.Meditation.route) { MeditationScreen(navController) }
        composable(Screen.Focus.route) { FocusSessionScreen(navController) }
        composable(Screen.DigitalTwin.route) { DigitalLifestyleTwinScreen(navController) }
        composable(Screen.BehaviorDrift.route) { BehaviorAnalysisScreen(navController) }
        composable(Screen.StressLikelihood.route) { StressLikelihoodScreen(navController) }
        composable(Screen.CoachChat.route) { AICoachChatScreen(navController) }
        composable(Screen.Goals.route) { GoalsScreen(navController) }
        composable(Screen.Achievements.route) { AchievementsScreen(navController) }
        composable(Screen.Reports.route) { ReportsScreen(navController) }
        composable(Screen.EmergencyContacts.route) { EmergencyContactsScreen(navController) }
        composable(Screen.EmergencyMap.route) { EmergencyMapScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
    }
}
