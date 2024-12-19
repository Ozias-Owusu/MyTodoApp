package com.persol.mytodoapp.screens


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.persol.mytodoapp.R
import com.persol.mytodoapp.ViewModelProvider


import com.persol.mytodoapp.network.Post
import com.persol.mytodoapp.viewModels.PostUiState
import com.persol.mytodoapp.viewModels.PostViewModel
import com.persol.mytodoapp.viewModels.UserModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavController,

) {
    var showOptions by remember { mutableStateOf(false) }
    var selectedPost = Post(0, 0, "", "")
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showForm by remember { mutableStateOf(false) }

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
                        onClick = { navController.navigate("homePage") }
                    )
                    {Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )}
                },

            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showForm = true
                    },
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Add"
                    )
                }
        }
    ) {
        innerPadding ->
        val viewModel: PostViewModel = viewModel(factory = ViewModelProvider.Factory)
        when (val postUiState = viewModel.postUiState) {
            is PostUiState.Success -> PostList(
                posts = postUiState.posts,
                Modifier.padding(innerPadding),
                onLongPress = { showOptions = true},
                onEditDone = {
                    scope.launch {
                        snackbarHostState.showSnackbar(viewModel.updatePost(selectedPost))
                        println("DANGGGGGGGG ${selectedPost.body}")
                    }
                    showOptions = false
                },
                selectedPost = selectedPost,
                onDeleteDone = {
                    scope.launch {
                        snackbarHostState.showSnackbar(viewModel.deletePost(selectedPost))
                    }
                    showOptions = false
                }
                )

            is PostUiState.Error -> ErrorScreen(retryAction = viewModel::getPostItems)
            is PostUiState.Loading -> LoadingScreen()
        }
        if (showForm) {
            var title by remember { mutableStateOf(selectedPost.title) }
            var body by remember { mutableStateOf(selectedPost.body) }
            PostEditingFields(
                formTitle = "Create Post",
                title = title,
                body = body,
                onDismiss = { showForm = false },
                selectedPost = selectedPost,
                onValueChange = {title= it },
                onBodyChange = { body = it },
                onEdit = {
                    scope.launch {
                        viewModel.createPost(selectedPost)
                        snackbarHostState.showSnackbar(viewModel.createPost(selectedPost))
                    }
                    showForm = false
                }
            )
        }
    }

}
// editing post screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostOptionsDialog(
    selectedPost: Post?,
    onEditSelected: () -> Unit,
    onDeleteSelected: () -> Unit
) {
    var thisPost : Post? = Post(0, 0, "", "")
    val viewModel: PostViewModel = viewModel(factory = ViewModelProvider.Factory)
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = { },
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 10.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Post Options",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
                )
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = {
                    thisPost = selectedPost
                    onEditSelected()
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )

                ) {
                Text(text = "Edit")
            }
            Button(
                onClick = {
                    thisPost = selectedPost
                    println("hIIIII ${thisPost!!.title}")
                    onDeleteSelected()},
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )

            ) {
                Text(text = "Delete")
            }


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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostListItem(
    post: Post,
    modifier: Modifier = Modifier,
    onLongPress: () -> Unit = {},

) {
    val user = UserModel.getUser(post.userId)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = {
                    onLongPress()
                    println("Hello you fucking clicked me!!!!!!!!")
                },
                onClick = {},
                onDoubleClick = {},

                ),


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
    modifier: Modifier = Modifier,
    onLongPress: () -> Unit,
    selectedPost: Post?,
    onEditDone: () -> Unit,
    onDeleteDone: () -> Unit
) {
    var thisPost by remember { mutableStateOf(selectedPost) }
    var showOptions by remember { mutableStateOf(false) }
    var editingDialog by remember { mutableStateOf(false) }
    val viewModel: PostViewModel = viewModel(factory = PostViewModel.Factory)
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(posts.size) { index ->
            PostListItem(
                post = posts[index],
                onLongPress = {
                    showOptions = true
                    thisPost = posts[index]
                    println(thisPost!!.title)
                    onLongPress()
                }
            )
        }
    }
    if (showOptions) {
        PostOptionsDialog(
            selectedPost = thisPost,
            onDeleteSelected = {
                showDeleteDialog = true
                showOptions = false
            },
            onEditSelected = {
                editingDialog = true
                showOptions = false
            }
        )
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Todo") },
            text = { Text("Are you sure you want to delete this post?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        scope.launch {
                            println(thisPost!!.title)
                            viewModel.deletePost(thisPost!!)
                            println(viewModel.deletePost(thisPost!!))
                            onDeleteDone()
                        }

                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    if (editingDialog) {
        var title by remember { mutableStateOf(thisPost!!.title) }
        var body by remember { mutableStateOf(thisPost!!.body) }
        PostEditingFields(
            formTitle = "Edit Post",
            title = title,
            body = body,
            onEdit = {
               scope.launch {
                   viewModel.updatePost(thisPost!!)
                   println(thisPost!!.id)
                   editingDialog = false
                   onEditDone()
               }
            },
            onDismiss = { editingDialog = false },
            selectedPost = thisPost,
            onValueChange = {title = it },
            onBodyChange = { body = it }
        )
    }
}

// Post Editing fields
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditingFields(
    formTitle: String,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDismiss: () -> Unit,
    selectedPost: Post?,
    onValueChange: (String) -> Unit,
    onBodyChange: (String) -> Unit

) {
    var selectedPostItem by remember { mutableStateOf(selectedPost) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        scrimColor = Color.Transparent,
        sheetState = SheetState(
            skipPartiallyExpanded = true,
            density = LocalDensity.current
        )
    ) {
        Column(
            modifier = modifier
                .padding(10.dp),
           horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            OutlinedTextField(
                value = title,
                onValueChange = {
                    onValueChange(it)
                },
                label = { Text(text = "Title") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                value = body,
                onValueChange = {
                    onBodyChange(it)
                },
                label = { Text(text = "Body") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(10.dp))

            errorMessage?.let {
                Text(text = it, color = Color(0xFFFD4F4F), fontSize = 15.sp)
                Spacer(modifier = Modifier.padding(5.dp))
            }
            Spacer(modifier = Modifier.padding(10.dp))

            Button(
                onClick = {
                    if(title.isNotBlank() && body.isNotBlank()) {
                        onEdit()
                    } else{
                        errorMessage = " Please fill all fields"
                    }

                }
            ) {
                Text (text = "Save")

            }
        }
    }

}

