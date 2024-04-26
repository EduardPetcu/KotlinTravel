package com.example.travel.repository.Images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

class ImageRepositoryImpl : ImageRepository {
    override suspend fun loadImageFromFirebaseStorage(
        path: String
    ): Uri? {
        return try {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReference(path)
            val imageUri = storageRef.downloadUrl.await()

            Log.d("Image Repository", "Image uri is: $imageUri")
             imageUri
        } catch (e: Exception) {
            e.printStackTrace();
            null
        }
    }

    override fun uploadImageToFirebaseStorage(context: Context, path: String, imageUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference(path)

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                //Log.d("ImageRepoImpl", {it.metadata?.path})
            }
            .addOnFailureListener {
            }
    }
}