package com.example.livesportexercise

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.livesportexercise.data.QueryItem

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController
) {
    var text by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf(FilterType.All)}

    Column {
        SearchField(
            text = text,
            filter = filter,
            onTextChange = { text = it },
            viewModel = viewModel,
        )
        Row() {
            TagButton(FilterType.All, filter, onTabSelected = {filter = FilterType.All})
            TagButton(FilterType.Competition, filter, onTabSelected = {filter = FilterType.Competition})
            TagButton(FilterType.Participants, filter, onTabSelected = {filter = FilterType.Participants})
        }

        when (val state = viewModel.uiState.collectAsState().value){
            is SearchViewModel.SearchState.Loaded -> SearchLoadedScreen(data = state.data, navController)
            is SearchViewModel.SearchState.Loading -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            is SearchViewModel.SearchState.Error -> ErrorDialog(
                state.message,
                onDismiss = {viewModel.resetLoadingState()},
                onTryAgain = {viewModel.getSearchResults(text, filter.ids)}
            )
            else -> {}
        }
    }
}

enum class FilterType(val ids: String, val title: String) {
    All(ids = Config.allIds, title = "Všetky"),
    Competition(ids = Config.competitions, title = "Souteže" ),
    Participants(ids = Config.participants, title = "Participanti"),
}

@Composable
fun ErrorDialog(
    message : String,
    onDismiss: () -> Unit,
    onTryAgain:() -> Unit
){
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                openDialog.value = false
            },
            title = {
                Text(text = "Something went wrong")
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        onDismiss()
                        onTryAgain()
                    }) {
                    Text("Try again")
                }
            }
        )
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String,
    filter: FilterType,
    onTextChange: (String) -> Unit,
    viewModel: SearchViewModel
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 6.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "",
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        onTextChange("")
                    }
                )
            },
            textStyle = MaterialTheme.typography.h6.copy(
                fontSize = 16.sp,
            ),
            modifier = modifier
                .fillMaxWidth(0.7f)
        )
        OutlinedButton(
            onClick = { viewModel.getSearchResults(text, filter.ids) },
            modifier = Modifier
                .fillMaxWidth(0.9f),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text(
                text = AnnotatedString("Hledat")
            )
        }
    }
}

@Composable
fun TagButton(
    filter: FilterType,
    selectedTab: FilterType,
    onTabSelected: () -> Unit
){
    val isSelected = filter == selectedTab
    OutlinedButton(
        onClick = {
            onTabSelected()
        },
        modifier = Modifier
            .background(Color.Transparent)
            .padding(horizontal = 4.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color.White else Color.Gray
        )
    )
        {
        Text(
            text = filter.title,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchLoadedScreen(data: List<QueryItem>, navController: NavController){
    val grouped = data.groupBy { it.sport.name }
    LazyColumn {
        grouped.forEach { (initial, contactsForInitial) ->
            stickyHeader {
                CategoryHeader(initial)
            }

            items(contactsForInitial) { contact ->
                SearchItem(contact, navController)
            }
        }
    }
}

@Composable
fun CategoryHeader(title: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Text(
            text = title,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 0.dp)
        )
    }
}

@Composable
fun SearchItem(item : QueryItem, navController: NavController){
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("detail/${item.id}")
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("${Config.baseImageUrl}${if (item.images.isNotEmpty()) item.images[0].path else ""}")
                .crossfade(true)
                .build(),
            contentDescription = "",
            placeholder = painterResource(R.drawable.ic_person_placeholder),
            error = painterResource(R.drawable.ic_person_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .background(Color.White)
        )
        Text(
            text = item.name,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(start = 6.dp)
        )
    }
    Divider()
}
