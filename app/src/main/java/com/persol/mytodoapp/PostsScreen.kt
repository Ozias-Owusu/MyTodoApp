package com.persol.mytodoapp


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage


import com.persol.mytodoapp.network.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavController
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Posts",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        style = MaterialTheme.typography.titleLarge)
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer

                ),
                navigationIcon = {
                    IconButton(
                        onClick = (
                                { navController.navigate("home_Page") }
                                )
                    )
                    {Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )}
                }
            )
        }
    ) {
        innerPadding ->
        val viewModel: PostViewModel = viewModel(factory = PostViewModel.Factory)
        when (val postUiState = viewModel.postUiState) {
            is PostUiState.Success -> PostList(posts = postUiState.posts, Modifier.padding(innerPadding))
            is PostUiState.Error -> ErrorScreen(retryAction = viewModel::getPostItems)
            is PostUiState.Loading -> LoadingScreen()
        }
    }

}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = ("loading failed"), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.loading_img), contentDescription = null
        )
        Text(text = "Loading")
    }
}

@Composable
fun PostListItem(
    post: Post,
    modifier: Modifier = Modifier

) {
    val user = UserModel.getUser(post.userId)
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box (
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(
                            RoundedCornerShape(50.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50.dp)
                        )
                ){
                    AsyncImage(
                        model = user.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(80.dp)
                    )

                }
                Text(text = user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = post.title,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp))
            Text(text = post.body, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun PostList(
    posts: List<Post>,
    modifier: Modifier = Modifier

) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(posts.size) { index ->
            PostListItem(post = posts[index])
        }
    }
}