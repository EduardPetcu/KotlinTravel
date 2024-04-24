package com.example.travel.screens


import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.travel.R
import com.example.travel.components.CurrentLocationScreen
import com.example.travel.components.DesignComponents.LaunchAlert
import com.example.travel.components.ProfileContent
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.data.user.SearchViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.repository.Images.ImageRepositoryImpl
import com.example.travel.ui.theme.ContainerYellow
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.TravelTheme
import com.example.travel.ui.theme.UserProfile

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(loginViewModel: LoginViewModel = viewModel()) {
    var showSignOutDialog by remember { mutableStateOf(false) }
    val searchViewModel = viewModel<SearchViewModel>()
    val searchText by searchViewModel.searchText.collectAsState()
    val users by searchViewModel.users.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()
    val imageRepositoryImpl = ImageRepositoryImpl()
    TravelTheme {
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
                Row {
                    UserProfile()
                    Spacer(modifier = Modifier.padding(horizontal = 30.dp))
                    IconButton(onClick = {
                          showSignOutDialog = true },
                        modifier = Modifier.padding(vertical = 18.dp)) {
                        Icon(imageVector = Icons.AutoMirrored.Default.Logout, contentDescription = "Sign Out", tint = Color.White)
                    }
                }
                Column( modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    TextField(value = searchText,
                        onValueChange = searchViewModel::onSearchTextChange,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.LightGray),
                        placeholder = { Text("Search for users", color = Color.LightGray) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isSearching) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn (
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(users) { user ->
                                val imageBitmap = user.imagePicture?.asImageBitmap() ?: BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.standard_pfp).asImageBitmap()
                                Card (
                                    colors = CardDefaults.cardColors(
                                        containerColor = ContainerYellow
                                    ),
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
                                        // check if user has image and if not load default image from res/drawable/standard_pf.png
                                        Image(bitmap = imageBitmap,
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
                                    // user.
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    CurrentLocationScreen()
                    if (showSignOutDialog) {
                         LaunchAlert("Sign Out", "Are you sure you want to sign out?", "Sign Out", "Cancel", onConfirm = {
                             loginViewModel.onEvent(LoginUIEvent.LogoutClicked)
                             TravelAppRouter.navigateTo(Screen.LoginScreen)
                         }, onDismissRequest = { showSignOutDialog = false })
                    }
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

