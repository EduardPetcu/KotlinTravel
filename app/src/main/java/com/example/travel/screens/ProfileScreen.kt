package com.example.travel.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.travel.R
import com.example.travel.data.User
import com.example.travel.data.login.LoginUIEvent
import com.example.travel.data.login.LoginViewModel
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.ui.theme.TabBarItem
import com.example.travel.ui.theme.TabView
import com.example.travel.ui.theme.UserProfile
import com.example.travel.ui.theme.fetchUserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

val tabBarItems = listOf(TabBarItem.homeTab, TabBarItem.calculteTab, TabBarItem.transportTab, TabBarItem.profileTab)
@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(loginViewModel: LoginViewModel = viewModel()) {
    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        bottomBar = { TabView(tabBarItems = tabBarItems, selectedTabIndex = 3) },
        containerColor = Color.hsl(236f, 0.58f, 0.52f)
    ) {
        padding -> ProfileContent(
            modifier = Modifier
                .padding(padding)
        ) {
            TopProfileLayout()
        }


    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopProfileLayout() {
    var userInfo by remember { mutableStateOf<User?>(null) }
    val context = LocalContext.current
    var imageBitmap: android.graphics.Bitmap? by remember { mutableStateOf(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            if (uri != null) {
                val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
                uploadImageToFirebaseStorage(context, path, uri)
            }
        }

    LaunchedEffect(key1 = true) {
        fetchUserData { user ->
            userInfo = user
        }
        loadImage(context, "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid) { loadedBitmap ->
            imageBitmap = loadedBitmap
        }
    }

    Surface(
        shape = RoundedCornerShape(8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = Color(0xFFD5C28C)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.travellogo),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .size(60.dp)
//                )
                if (imageBitmap == null) {
                    // show a loading icon
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp)
                            .background(Color.Black)
                    )
                } else {
                    imageBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(60.dp)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                        Log.d("ProfileScreen", "Image loaded")
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = userInfo?.username ?: "Loading...",
                        style = MaterialTheme.typography.h5
                    )

                    Text(
                        text = userInfo?.userRole ?: "Loading...",
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            FlowRow(modifier = Modifier.padding(vertical = 5.dp)) {
                ImageTextContent(
                    modifier = Modifier.padding(vertical = 5.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "emaill",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            text = userInfo?.email ?: "Loading...",
                            style = MaterialTheme.typography.h5,
                        )
                    }
                )
                ImageTextContent(
                    modifier = Modifier.padding(vertical = 5.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "locationn",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            text = "Romania, Bucharest",
                            style = MaterialTheme.typography.h5,
                        )
                    }
                )
            }
//            FlowRow(modifier = Modifier.padding(vertical = 5.dp)) {
//                Text(text = "About me", style = MaterialTheme.typography.h6)
//                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit description", tint = Color.Blue)
//            }
        }

    }
}


@Composable
fun ImageTextContent(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(modifier = Modifier.width(5.dp))
        text()
        Spacer(modifier = Modifier.width(10.dp))
    }
}

suspend fun loadImageFromFirebaseStorage(
    path: String
): Bitmap? {
    return try {
        val storage = FirebaseStorage.getInstance()
        // Log the path
        Log.d("ProfileScreen", "Path: $path")
        val storageRef = storage.getReference(path)

        val localFile = File.createTempFile("images", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            Log.d("ProfileScreen", "Image downloaded")
            // Image download successful
        }.addOnFailureListener {

        }.await()

        // Convert the file to Bitmap
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath, options)

        // Return the bitmap
        return bitmap
    } catch (e: Exception) {
        e.printStackTrace();
        null
    }
}

fun loadImage(content: Context, path: String, onImageLoaded: (Bitmap?) -> Unit) {
    GlobalScope.launch {
        val bitmap = loadImageFromFirebaseStorage(path)
        withContext(Dispatchers.Main) {
            onImageLoaded(bitmap)
        }
    }
}
fun uploadImageToFirebaseStorage(context: Context, path: String, imageUri: Uri) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.getReference(path)

    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            Log.d("ProfileScreen", "Image uploaded")
        }
        .addOnFailureListener {
            Log.e("ProfileScreen", "Image upload failed", it)
        }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
