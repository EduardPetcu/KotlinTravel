package com.example.travel.repository.Images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

class ImageRepositoryImpl : ImageRepository {
    override suspend fun loadImageFromFirebaseStorage(
        path: String
    ): Bitmap? {
        return try {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReference(path)

            val localFile = File.createTempFile("images", "jpg")
            storageRef.getFile(localFile).addOnSuccessListener {
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

    override fun uploadImageToFirebaseStorage(context: Context, path: String, imageUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference(path)

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }
}