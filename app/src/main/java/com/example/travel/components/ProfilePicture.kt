package com.example.travel.components

import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.travel.R
import com.example.travel.data.User
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
fun RenderPicture(isMe: Boolean, userInfo: User?) {
    val imageRepositoryImpl = ImageRepositoryImpl()
    val databaseRepositoryImpl = DatabaseRepositoryImpl()
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    if (isMe) {
        LaunchedEffect(key1 = true) {
            val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
            loadImage(context, path, onImageLoaded = {
                imageUri = it
            }, imageRepositoryImpl)
        }
    } else {
        imageUri = Uri.parse(userInfo?.imagePicture.toString())
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
                imageRepositoryImpl.uploadImageToFirebaseStorage(context, path, uri)
                // imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(launcher) {
        val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference(path)
        val uriProfile = storageRef.downloadUrl.await()
        imageUri = uriProfile
        databaseRepositoryImpl.updateUserData(mapOf("imagePicture" to uriProfile))
    }

    if (imageUri == null) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .clickable {
                    if (isMe) {
                        launcher.launch("image/*")
                    }
                }
        )
    } else {
        imageUri?.let {
            GlideImage(model = imageUri, contentDescription = "Profile picture",
                failure = placeholder(R.drawable.standard_pfp),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (isMe) {
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
        val uri = imageRepositoryImpl.loadImageFromFirebaseStorage(path)
        withContext(Dispatchers.Main) {
            onImageLoaded(uri)
        }
    }
}

