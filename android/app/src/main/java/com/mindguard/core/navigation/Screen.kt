package com.mindguard.core.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Lifestyle : Screen("lifestyle")
    object Mood : Screen("mood")
    object Journal : Screen("journal")
    object Meditation : Screen("meditation")
    object Focus : Screen("focus")
    object DigitalTwin : Screen("digital_twin")
    object BehaviorDrift : Screen("behavior_drift")
    object StressLikelihood : Screen("stress_likelihood")
    object CoachChat : Screen("coach_chat")
    object Goals : Screen("goals")
    object Achievements : Screen("achievements")
    object Reports : Screen("reports")
    object EmergencyContacts : Screen("emergency_contacts")
    object EmergencyMap : Screen("emergency_map")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
