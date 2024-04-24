package com.example.travel.components

import android.content.Context
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.travel.repository.Images.ImageRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun RenderPicture() {
    val imageRepositoryImpl: ImageRepositoryImpl = ImageRepositoryImpl()
    val context = LocalContext.current
    var imageBitmap: Bitmap? by remember { mutableStateOf(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = true) {
        val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
        loadImage(context, path, onImageLoaded = {
            imageBitmap = it
        }, imageRepositoryImpl)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            if (uri != null) {
                val path = "ProfilePicture/" + FirebaseAuth.getInstance().currentUser!!.uid
                imageRepositoryImpl.uploadImageToFirebaseStorage(context, path, uri)
                imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        }

    if (imageBitmap == null) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .clickable {
                    launcher.launch("image/*")
                }
        )
    } else {
        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun loadImage(content: Context, path: String, onImageLoaded: (Bitmap?) -> Unit, imageRepositoryImpl: ImageRepositoryImpl) {
    GlobalScope.launch {
        val bitmap = imageRepositoryImpl.loadImageFromFirebaseStorage(path)
        withContext(Dispatchers.Main) {
            onImageLoaded(bitmap)
        }
    }
}

