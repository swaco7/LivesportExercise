package com.example.livesportexercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.livesportexercise.data.QueryItem


@Composable
fun DetailScreen(item: QueryItem){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
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
                .size(100.dp)
                .background(Color.White)
        )
        Text(text = item.name)
        Text(text = item.sport.name)
        Text(text = item.country.name)
    }
}