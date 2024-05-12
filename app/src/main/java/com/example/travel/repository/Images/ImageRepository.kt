package com.example.travel.repository.Images

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.Deferred

interface ImageRepository {
    fun uploadImageToFirebaseStorage(context: Context, path: String, imageUri: Uri) : Deferred<Unit>
    suspend fun loadImageFromFirebaseStorage(path: String): Uri?
}