package com.example.travel.repository.Images

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {
    fun uploadImageToFirebaseStorage(context: Context, path: String, imageUri: Uri)
    suspend fun loadImageFromFirebaseStorage(path: String): Uri?
}