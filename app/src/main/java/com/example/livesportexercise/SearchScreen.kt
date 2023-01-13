package com.example.livesportexercise

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.network.LivesportRepository
import com.example.livesportexercise.network.liveSportService

@Composable
fun SearchScreen() {
    val repository = LivesportRepository(livesportService = liveSportService)
    val viewModel by remember { mutableStateOf(SearchViewModel(repository)) }
    var text by remember { mutableStateOf("") }

    Column {
        SearchField(
            text = text,
            onTextChange = { text = it },
            viewModel = viewModel
        )

        when (val state = viewModel.uiState.collectAsState().value){
            is SearchViewModel.SearchState.Loaded -> SearchLoadedScreen(data = state.data)
            else -> {}
        }
    }



}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    viewModel: SearchViewModel
){
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = ""
                )
            },
            textStyle = MaterialTheme.typography.h6.copy(
                fontSize = 16.sp,
            ),
            modifier = modifier.background(Color.White)
        )
        ClickableText(
            text = AnnotatedString("SearchScreen"),
            onClick = {
                viewModel.getSearchResults()
            }
        )
    }

}

@Composable
fun SearchLoadedScreen(data: List<QueryItem>){
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(data) { item ->
            Column {
                Text(item.id)
            }
        }
    }
}
