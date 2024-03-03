package com.example.travel.repository

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class ProfileImageRepositoryImpl {
    suspend fun loadImageFromFirebaseStorage(
        path: String
    ): Bitmap? {
        return try {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReference(path)

            val localFile = File.createTempFile("images", "jpg")
            storageRef.getFile(localFile).addOnSuccessListener {
                // Image download successful
            }.addOnFailureListener {

            }.await() // Make sure to call this function from a coroutine or use .addOnCompleteListener

            // Convert the file to Bitmap
            val options = android.graphics.BitmapFactory.Options()
            val bitmap = android.graphics.BitmapFactory.decodeFile(localFile.absolutePath, options)

            // Return the bitmap
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace();
            null
        }

    }
}
