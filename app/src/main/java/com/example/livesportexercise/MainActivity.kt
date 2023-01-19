package com.example.livesportexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.livesportexercise.ui.theme.LivesportExerciseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LivesportExerciseTheme {
                // A surface container using the 'background' color from the theme
                //val owner = LocalViewModelStoreOwner.current

//                owner?.let {
//                    val viewModel: SearchViewModel = viewModel(
//                        it,
//                        "MainViewModel",
//                        SearchViewModelFactory(LivesportRepository(livesportService = liveSportService)
//                    ))
                    AppContainer()
                //}
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LivesportExerciseTheme {
        Greeting("Android")
    }
}