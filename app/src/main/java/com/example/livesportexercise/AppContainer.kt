package com.example.livesportexercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.network.LivesportRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi


@Composable
fun AppContainer(viewModel: SearchViewModel = viewModel()){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: ""
    Scaffold(
        topBar = {
            CustomTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "Výsledky",
            modifier = Modifier.padding(padding)
        ) {
            composable(route = "Výsledky") {
                SearchScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable(route = "detail/{itemId}") { navBackStackEntry ->
                val itemId = navBackStackEntry.arguments?.getString("itemId")
                itemId?.let {
                    val state = viewModel.uiState.collectAsState().value
                    if (state is SearchViewModel.SearchState.Loaded) {
                        val item = state.data.find { it.id == itemId }
                        item?.let {
                            DetailScreen(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTopAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(if (!canNavigateBack) currentScreen else "Detail") },
        backgroundColor = Color.DarkGray,
        contentColor = Color.White,
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }
        }
    )
}