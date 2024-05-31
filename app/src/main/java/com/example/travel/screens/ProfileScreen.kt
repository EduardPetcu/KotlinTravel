package com.example.travel.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.travel.components.DesignComponents.LaunchAlert
import com.example.travel.components.ProfileComponents.AchievementsLayout
import com.example.travel.components.ProfileComponents.DescriptionText
import com.example.travel.components.ProfileComponents.FullScreenImage
import com.example.travel.components.ProfileComponents.TopProfileLayout
import com.example.travel.components.ProfileContent
import com.example.travel.data.Achievement
import com.example.travel.data.Post
import com.example.travel.data.User
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.repository.Images.ImageRepositoryImpl
import com.example.travel.repository.PostRepositoryImpl
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.ContainerYellow
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

val tabBarItems = listOf(TabBarItem.homeTab, TabBarItem.calculteTab, TabBarItem.mapTab, TabBarItem.profileTab)
@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun ProfileScreen(user: User? = null) {
    val showDeleteDialog = remember { mutableStateOf(false) }
    var locPicture = mutableMapOf<String, List<String>>()
    val uriChosen = remember { mutableStateOf<Uri?>(null) }
    val imageClicked = remember { mutableStateOf(false) }
    var updatedAchievements by remember { mutableStateOf(false) }
    var postListDisplayed = remember { mutableStateOf(listOf<Post>()) }
    var followedList by remember { mutableStateOf(listOf<String>()) }
    val databaseRepositoryImpl = DatabaseRepositoryImpl()
    val imageRepositoryImpl = ImageRepositoryImpl()
    val postRepositoryImpl = PostRepositoryImpl()
    val context = LocalContext.current

    BackHandler (
        onBack = {
            TravelAppRouter.navigateTo(Screen.HomeScreen)
        })

    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 3) },
        containerColor = BackgroundBlue
    ) {
        padding -> ProfileContent(
            modifier = Modifier
                .padding(padding)
        ) {
            val path = remember { mutableStateOf("") }
            var userInfo by remember { mutableStateOf<User?>(null) }
            val cityImage : MutableState<String> = remember { mutableStateOf("") }
            val imageUploaded = remember { mutableStateOf(false) }
            val uploadingProcess = remember { mutableStateOf("") }
            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                    if (uri != null) {
                        GlobalScope.launch {
                            // assign uploadingProcess variable the value of cityImage
                            uploadingProcess.value = cityImage.value
                            imageRepositoryImpl.uploadImageToFirebaseStorage(context, path.value, uri).await()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show()
                                imageUploaded.value = true
                            }
                        }
                    }
                }
            LaunchedEffect(imageUploaded.value) {
                if (path.value != "" && imageUploaded.value) {
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.getReference(path.value)
                    // render a spinner while the image is loading
                    val uriProfile = storageRef.downloadUrl.await()
                    //locPicture[cityImage.value] = locPicture.getOrDefault(cityImage, listOf()) + uriProfile.toString()
                    databaseRepositoryImpl.updateUserData(mapOf("locationPicture" to locPicture))
                    val newPost = Post(userId = userInfo!!.id,
                        username = userInfo!!.username,
                        image = uriProfile.toString(),
                        location = cityImage.value,
                        userProfilePicture = userInfo?.imagePicture)
                    postListDisplayed.value += newPost
                    postRepositoryImpl.insertPost(newPost)
                    // update the user info
                    uploadingProcess.value = ""
                    imageUploaded.value = false
                }
            }
            LaunchedEffect(key1 = true) {
                val userDeferred = async { databaseRepositoryImpl.fetchUserInfo() }
                userInfo = userDeferred.await()
                if (user == null) { // AM SCOS async din jurul if-ului
                    locPicture = userInfo!!.locationPicture.toMutableMap()
                    userInfo = updateAchievements(userInfo!!)
                    updatedAchievements = true
                    val postListDeferred = async { postRepositoryImpl.getPostsFromList(listOf(userInfo!!.username)) }
                    postListDisplayed.value = postListDeferred.await()
                    Log.d("ProfileScreen", "postListDisplayed size: ${postListDisplayed.value.size}")
                } else {
                    followedList = userInfo!!.followedUsers
                    userInfo = user
                    locPicture = userInfo!!.locationPicture.toMutableMap()
                    updatedAchievements = true
                    val postListDeferred = async { postRepositoryImpl.getPostsFromList(listOf(user.username)) }
                    postListDisplayed.value = postListDeferred.await()
                    Log.d("ProfileScreen", "postListDisplayed size: ${postListDisplayed.value.size}")
                }
            }
            LazyColumn {
                if (user == null) {
                    item {
                        TopProfileLayout(userInfo, context, isMe = true, userInfo?.followedUsers)
                    }
                    item {
                        DescriptionText(userInfo, context, true)
                    }
                } else {
                    item {
                        TopProfileLayout(userInfo, context, false, followedList)
                    }
                    item {
                        DescriptionText(userInfo, context, false)
                    }
                }
                if (updatedAchievements) {
                    item {
                        AchievementsLayout(userInfo)
                    }
                }
                if (userInfo != null) {
                    item {
                        DisplayImagesFromVisitedCities(
                            userInfo = userInfo!!,
                            listPosts = postListDisplayed,
                            isMe = user == null,
                            cityImage = cityImage,
                            path = path,
                            uploadingProcess = uploadingProcess,
                            uriChosen = uriChosen,
                            imageClicked = imageClicked,
                            launcher = launcher,
                            postRepositoryImpl = postRepositoryImpl
                        )
                    }
                }
            }
        if (imageClicked.value) {
                FullScreenImage(
                    uri = uriChosen.value!!,
                    onDismiss = { imageClicked.value = false },
                    modifier = Modifier.clip(RoundedCornerShape(8)))
            }
        }
    }
}

fun updateAchievements(userInfo: User) : User {
    val userAchievements = userInfo.achievements.toMutableList()
    val allAchievements = Achievement.achievements.map { achievement -> achievement.title }

    if (userInfo.visitedCities.size >= 5 && !userAchievements.contains(allAchievements[1])) {
        userAchievements += allAchievements[1] // visited 5 cities
    }
    if (userInfo.budgets.isNotEmpty() && !userAchievements.contains(allAchievements[2])) {
        userAchievements += allAchievements[2] // planned a budget
    }
    if (userInfo.visitedCities.size >= 10 && !userAchievements.contains(allAchievements[3])) {
        userAchievements += allAchievements[3] // visited 10 cities
    }

    val updatedUser = userInfo.copy(achievements = userAchievements)
    val userDatabase = DatabaseRepositoryImpl()
    userDatabase.updateUserData(updatedUser)
    return updatedUser
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DisplayImagesFromVisitedCities(userInfo: User,
                                   listPosts: MutableState<List<Post>>,
                                   isMe: Boolean,
                                   cityImage : MutableState<String>,
                                   path: MutableState<String>,
                                   uploadingProcess : MutableState<String>,
                                   uriChosen: MutableState<Uri?>,
                                   imageClicked: MutableState<Boolean>,
                                   launcher: ActivityResultLauncher<String>,
                                   postRepositoryImpl: PostRepositoryImpl
) {
        val context: Context = LocalContext.current
        Surface(
            shape = RoundedCornerShape(8),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = ContainerYellow
        ) {
            Text(
                text = "Visited Cities",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
        for (city in userInfo.visitedCities) {
            Surface(
                shape = RoundedCornerShape(8),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                color = ContainerYellow
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row {
                        Text(
                            text = city,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        if (isMe) {
                            IconButton(
                                onClick = {
                                    val idPicture =
                                        System.currentTimeMillis().toString()
                                    cityImage.value = city
                                    path.value =
                                        "UserPictures/${userInfo!!.id}/$city/$idPicture"
                                    launcher.launch("image/*")
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                    if (uploadingProcess.value == city) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                                .align(Alignment.CenterHorizontally),
                            color = Color.Blue
                        )
                    }
                    LazyRow {
                        val cityPostList = listPosts.value.filter { post -> post.location == city }
                        for (post in cityPostList) {
                            val uri: Uri = Uri.parse(post.image)
                            item {
                                var showDeleteDialog by remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .size(150.dp)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(8))
                                        .clickable(
                                            onClick = {
                                                imageClicked.value = true
                                                uriChosen.value = uri
                                            }
                                        )
                                ) {
                                    GlideImage(
                                        model = uri,
                                        contentDescription = "image of city $city",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .border(
                                                1.dp,
                                                Color.Black,
                                                RoundedCornerShape(8)
                                            )
                                    )
                                    if (isMe) {
                                        IconButton(
                                            onClick = { showDeleteDialog = true },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.Red,
                                            )
                                        }
                                    }
                                }
                                if (showDeleteDialog) {
                                    LaunchAlert(
                                        title = "Delete image",
                                        question = "Are you sure you want to delete this image?",
                                        s1 = "Yes",
                                        s2 = "No",
                                        onConfirm = {
                                            postRepositoryImpl.deletePost(post.id)
                                            listPosts.value = listPosts.value.filter { it != post }
                                            Toast.makeText(context, "Image deleted", Toast.LENGTH_SHORT).show()
                                            showDeleteDialog = false
                                        },
                                        onDismissRequest = {
                                            showDeleteDialog = false
                                        }
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }
}
