package com.example.futtoprint

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futtoprint.post.Post
import com.example.futtoprint.post.PostRepository
import com.example.futtoprint.util.toDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuttoPrintApp(postRepository: PostRepository) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val scrollToTop = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
            )
        },
        bottomBar = {
            PostForm(postMessage = {
                coroutineScope.launch {
                    postRepository.insert(it)
                    scrollToTop.value = true
                }
            }, modifier = Modifier.padding(20.dp))
        },
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
    ) { innerPadding ->
        // FIXME render scrollToTop to run recomposition
        Text(
            text = scrollToTop.value.toString(),
            modifier = Modifier.size(0.dp).alpha(0f),
        )

        PostFeed(
            coroutineScope = coroutineScope,
            listState = listState,
            scrollToTop = scrollToTop,
            posts =
                runBlocking {
                    postRepository.selectAll().first()
                },
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(10.dp),
        )
    }
}

@Composable
fun PostFeed(
    coroutineScope: CoroutineScope,
    listState: LazyListState,
    scrollToTop: MutableState<Boolean>,
    posts: List<Post>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement =
            Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.Bottom,
            ),
        state = listState,
        reverseLayout = true,
        modifier = modifier.fillMaxSize(),
    ) {
        if (scrollToTop.value) {
            coroutineScope.launch {
                listState.animateScrollToItem(index = 0)
            }
            scrollToTop.value = false
        }

        items(
            count = posts.size,
            key = { posts[it].id },
        ) { index ->
            val post = posts[index]
            Card(
                colors =
                    CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color.Gray),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                ) {
                    Text(text = post.timestamp.toDateTime(), fontSize = 12.sp, color = Color.Gray)
                    Text(text = post.content, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun PostForm(
    postMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var formText by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.wrapContentSize(),
    ) {
        OutlinedTextField(
            value = formText,
            onValueChange = { formText = it },
            suffix = {
                Button(
                    onClick = {
                        postMessage(formText)
                        formText = ""
                    },
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(imageVector = Icons.Rounded.Send, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(80.dp),
        )
    }
}
