package com.example.travel.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.travel.components.ProfileComponents.AchievementsLayout
import com.example.travel.components.ProfileComponents.DescriptionText
import com.example.travel.components.ProfileComponents.TopProfileLayout
import com.example.travel.components.ProfileContent
import com.example.travel.data.Achievement
import com.example.travel.data.User
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.repository.Images.ImageRepositoryImpl
import com.example.travel.ui.theme.BackgroundBlue
import com.example.travel.ui.theme.ContainerYellow
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView
import com.google.firebase.auth.FirebaseAuth
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(user: User? = null) {
    var locPicture = mutableMapOf<String, List<String>>()
    var updatedAchievements by remember { mutableStateOf(false) }
    val databaseRepositoryImpl = DatabaseRepositoryImpl()
    val imageRepositoryImpl = ImageRepositoryImpl()
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
            var path by remember { mutableStateOf("") }
            var userInfo by remember { mutableStateOf<User?>(null) }
            var cityImage by remember { mutableStateOf("") }
            var imageUploaded by remember { mutableStateOf(false) }

            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                    if (uri != null) {
                        GlobalScope.launch {
                            imageRepositoryImpl.uploadImageToFirebaseStorage(context, path, uri).await()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show()
                                imageUploaded = true
                            }
                        }
                    }
                }
            LaunchedEffect(imageUploaded) {
                if (path != "" && imageUploaded) {
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.getReference(path)
                    val uriProfile = storageRef.downloadUrl.await()
                    Log.d("Line 111:", locPicture.toString())
                    locPicture[cityImage] = locPicture.getOrDefault(cityImage, listOf()) + uriProfile.toString()
                    Log.d("Line 113:", locPicture.toString())
                    // userInfo = userInfo!!.copy(locationPicture = locPicture)
                    databaseRepositoryImpl.updateUserData(mapOf("locationPicture" to locPicture))
                    // update the user info
                    imageUploaded = false
                }
            }
            if (user == null) {
                LaunchedEffect(key1 = true) {
                    val userDeferred = async { databaseRepositoryImpl.fetchUserInfo() }
                    userInfo = userDeferred.await()
                    async {
                        locPicture = userInfo!!.locationPicture.toMutableMap()
                        Log.d("Line 126:", locPicture.toString())
                        userInfo = updateAchievements(userInfo!!)
                        updatedAchievements = true
                    }.await()
                }
            } else {
                userInfo = user
                updatedAchievements = true
            }
            LazyColumn {
                if (user == null) {
                    item {
                        TopProfileLayout(userInfo, context, true)
                    }
                    item {
                        DescriptionText(userInfo, context, true)
                    }
                } else {
                    item {
                        TopProfileLayout(userInfo!!, context, false)
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
                    }
                    for (city in userInfo!!.visitedCities) {
                        item {
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
                                        if (user == null) {
                                            IconButton(
                                                onClick = {
                                                    val idPicture =
                                                        System.currentTimeMillis().toString()
                                                    cityImage = city
                                                    path =
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
                                    LazyRow {
                                        for (image in locPicture.getOrDefault(city, listOf())) {
                                            val uri: Uri = Uri.parse(image)
                                            item {
                                                Box(
                                                    modifier = Modifier
                                                        .size(150.dp)
                                                        .padding(8.dp)
                                                        .clip(RoundedCornerShape(8))
                                                ) {
                                                    GlideImage(
                                                        model = uri,
                                                        contentDescription = "image of city $city",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .border(1.dp, Color.Black, RoundedCornerShape(8))
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
