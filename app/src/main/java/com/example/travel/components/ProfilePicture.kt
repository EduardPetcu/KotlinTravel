package com.example.travel.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.travel.R
import com.example.travel.data.User
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.repository.Images.ImageRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RenderPicture(isMe: Boolean, userInfo: User) {
    val imageRepositoryImpl = ImageRepositoryImpl()
    val databaseRepositoryImpl = DatabaseRepositoryImpl()
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val isOnProfileScreen = (TravelAppRouter.currentScreen.value == Screen.ProfileScreen)
    val uploadingPicture = remember { mutableStateOf(false) }
    imageUri = Uri.parse(userInfo.imagePicture)

    // This block of code does the image upload to the storage
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                uploadingPicture.value = true
                imageUri = uri
                val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
                imageRepositoryImpl.uploadImageToFirebaseStorage(context, path, uri)
                Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show()
            }
        }

    // This block of code is used to update imagePicture field in the database with the new imageUri
    LaunchedEffect(launcher) {
        if (uploadingPicture.value) {
            val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReference(path)
            val uriProfile = storageRef.downloadUrl.await()
            imageUri = uriProfile
            databaseRepositoryImpl.updateUserData(mapOf("imagePicture" to uriProfile))
            uploadingPicture.value = false
        }
    }

    if (imageUri == null) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .clickable {
                    if (isMe && isOnProfileScreen) {
                        launcher.launch("image/*")
                    }
                }
        )
    } else {
        imageUri?.let {
            GlideImage(model = imageUri, contentDescription = "Profile picture",
                failure = placeholder(R.drawable.standard_pfp),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (isMe && isOnProfileScreen) {
                            launcher.launch("image/*")
                        }
                    },
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun loadImage(content: Context, path: String, onImageLoaded: (Uri?) -> Unit, imageRepositoryImpl: ImageRepositoryImpl) {
    GlobalScope.launch {
        var uri = imageRepositoryImpl.loadImageFromFirebaseStorage(path)
        if (uri == null) {
            // If the user's profile picture doesn't exist, load the standard profile picture
            uri = imageRepositoryImpl.loadImageFromFirebaseStorage("ProfilePictures/standard_pfp.png")
        }
        withContext(Dispatchers.Main) {
            onImageLoaded(uri)
        }
    }
}
