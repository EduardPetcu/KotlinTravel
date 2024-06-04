package com.example.travel.repository.Images

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

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
            e.printStackTrace()
            null
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun uploadImageToFirebaseStorage(context: Context, path: String, imageUri: Uri) : Deferred<Unit> = GlobalScope.async {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference(path)

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                //Log.d("ImageRepoImpl", {it.metadata?.path})
            }
            .addOnFailureListener {
            }
            .await()
    }
}