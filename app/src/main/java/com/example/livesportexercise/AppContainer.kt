package com.example.livesportexercise

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppContainer(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable(route = "search") {
            SearchScreen(
            )
        }
        composable(route = "detail") {
            DetailScreen(

            )
        }
    }
}