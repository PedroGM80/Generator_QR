package dev.teckelsoft.generator_qr

sealed class DestinationScreen(val route: String) {
    object SplashScreenDest : DestinationScreen(route = "splash_screen")
    object MainScreenDest : DestinationScreen(route = "main_screen")
}