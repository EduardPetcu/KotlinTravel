package com.example.travel.screens


import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.travel.R
import com.example.travel.components.CurrentLocationScreen
import com.example.travel.components.DesignComponents.LaunchAlert
import com.example.travel.components.ProfileComponents.FullScreenImage
import com.example.travel.components.ProfileContent
import com.example.travel.data.Post
import com.example.travel.data.User
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.data.user.SearchViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.navigation.TravelAppRouter.navigateTo
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.repository.PostRepositoryImpl
import com.example.travel.ui.theme.ContainerYellow
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile
import kotlinx.coroutines.async
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(loginViewModel: LoginViewModel = viewModel()) {
    val uriChosen = remember { mutableStateOf<Uri?>(null) }
    val imageClicked = remember { mutableStateOf(false) }
    val postRepositoryImpl = PostRepositoryImpl()
    var showSearch by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    val searchViewModel = SearchViewModel()
    val searchText by searchViewModel.searchText.collectAsState()
    val users by searchViewModel.users.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()
    val databaseRepositoryImpl = DatabaseRepositoryImpl()
    var usersFollowedInfo by remember { mutableStateOf(listOf<User>()) }
    var followedUsers by remember { mutableStateOf(listOf<String>()) }
    var listPosts by remember { mutableStateOf(listOf<Post>()) }
    LaunchedEffect(key1 = true) {
        followedUsers = async { databaseRepositoryImpl.getFollowedUsers() }.await()
        listPosts = async { postRepositoryImpl.getPostsFromList(followedUsers) }.await()
        val usersFollowedInfoAux = async {databaseRepositoryImpl.getFollowedUsers(followedUsers) }.await()
        usersFollowedInfo = usersFollowedInfoAux
    }
    TravelTheme {
        BackHandler (
            onBack = {
                if (showSearch) {
                    showSearch = false
                }
            })
        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 0) },
        ) { padding ->
            ProfileContent(
                modifier = Modifier
                    .padding(padding)
            ) {
                Row() {
                    UserProfile(databaseRepositoryImpl = databaseRepositoryImpl, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        showSearch = true
                    },
                        modifier = Modifier.padding(vertical = 18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            showSignOutDialog = true
                        },
                        modifier = Modifier.padding(end = 8.dp).padding(vertical = 18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Logout,
                            contentDescription = "Sign Out",
                            tint = Color.White
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (showSearch) {
                        TextField(value = searchText,
                            onValueChange = searchViewModel::onSearchTextChange,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(textColor = Color.LightGray),
                            placeholder = { Text("Search for users", color = Color.LightGray) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (isSearching) {
                            Box(modifier = Modifier.size(100.dp)) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            LoadSearchedUsers(users)
                        }
                    } else {
                        if (listPosts.isNotEmpty()) {
                            Log.d("HomeScreen", "Users followed: $usersFollowedInfo")
                            RenderFollowedUserPictures(
                                posts = listPosts,
                                uriChosen = uriChosen,
                                imageClicked = imageClicked,
                                usersFollowedInfo = usersFollowedInfo
                            )
                        } else {
                            Text(
                                "No users followed yet",
                                style = MaterialTheme.typography.h6,
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    CurrentLocationScreen()
                }
                if (showSignOutDialog) {
                    LaunchAlert(
                        "Sign Out",
                        "Are you sure you want to sign out?",
                        "Sign Out",
                        "Cancel",
                        onConfirm = {
                            loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
                            TravelAppRouter.navigateTo(Screen.LoginScreen)
                        },
                        onDismissRequest = { showSignOutDialog = false })
                }
                if (imageClicked.value) {
                    FullScreenImage(
                        uri = uriChosen.value!!,
                        onDismiss = { imageClicked.value = false },
                        modifier = Modifier.clip(RoundedCornerShape(8))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadSearchedUsers(users: List<User>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
    ) {
        items(users) { user ->
            var uri: Uri? = null
            if (user.imagePicture != null)
                uri = Uri.parse(user.imagePicture.toString())
            Card (
                colors = CardDefaults.cardColors(
                    containerColor = ContainerYellow
                ),
                onClick = {
                    navigateTo(Screen.ProfileScreen, user)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .heightIn(min = 70.dp, max = 140.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(model = user.imagePicture,
                        contentScale = ContentScale.Crop,
                        failure = placeholder(R.drawable.standard_pfp),
                        contentDescription = "",
                        modifier = Modifier
                            .widthIn(max = 50.dp)
                            .heightIn(max = 50.dp)
                            .clip(CircleShape))
                    Text(
                        text = user.username + " (${user.email})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RenderFollowedUserPictures(posts: List<Post>,
                               uriChosen: MutableState<Uri?>,
                               imageClicked: MutableState<Boolean>,
                               usersFollowedInfo: List<User>) {
    // sort the posts by date (first post will be the most recent one)
    val sortedPosts = posts.sortedByDescending { it.timestamp }
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (post in sortedPosts) {
            val stamp = Instant.ofEpochMilli(post.timestamp)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
            val date = formatter.format(stamp)
            // get the user from usersFollowedInfo that has the same id as the post author
            val user = usersFollowedInfo.find { it.id == post.userId }
            Log.d("HomeScreen", "User image picture: ${user}")
            // convert stamp to date in format "dd/MM/yyyy"
            item {
                val uri = Uri.parse(post.image)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = ContainerYellow
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                   Column(
                       modifier = Modifier.padding(8.dp)
                   ) {
                       Row (
                           modifier = Modifier.padding(8.dp),
                       ) {
                           GlideImage( // profile picture
                               model = user?.imagePicture,
                               contentDescription = "",
                               contentScale = ContentScale.Crop,
                               failure = placeholder(R.drawable.standard_pfp),
                               modifier = Modifier
                                   .widthIn(max = 50.dp)
                                   .heightIn(max = 50.dp)
                                   .clip(CircleShape)
                           )
                           Spacer(modifier = Modifier.width(8.dp))
                           Column (modifier = Modifier.weight(1f)){
                               Text(
                                   text = post.username,
                                   style = MaterialTheme.typography.h6,
                               )
                               Text(
                                   text = post.location,
                                   style = MaterialTheme.typography.body1,
                               )
                           }
                           Text(text = date.toString(),
                                style = MaterialTheme.typography.body2,
                                )
                       }
                       GlideImage(
                           model = uri,
                           contentScale = ContentScale.Crop,
                           contentDescription = "",
                           modifier = Modifier
                               .size(275.dp)
                               .padding(8.dp)
                               .clip(RoundedCornerShape(8.dp))
                               .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                               .align(Alignment.CenterHorizontally)
                               .clickable(
                                   onClick = {
                                       imageClicked.value = true
                                       uriChosen.value = uri
                                   }
                               ))
                   }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
