package com.example.travel.repository

import android.util.Log
import com.example.travel.data.Post
import com.example.travel.data.User
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PostRepositoryImpl {
    private val db = Firebase.firestore

    suspend fun getPostsFromList(userList: List<String>) : List<Post> {
        val posts = mutableListOf<Post>()
        return try {
            val querySnapshot = db.collection("posts").get().await()
            querySnapshot?.toObjects(Post::class.java)
            for (document in querySnapshot!!) {
                val post = document.toObject<Post>()
                if (post.username in userList) {
                    posts.add(post)
                }
            }
            posts
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun insertPost(post: Post) {
        db.collection("posts").document(post.id).set(post)
            .addOnSuccessListener {
                Log.d("PostRepositoryImpl", "DocumentSnapshot added!")
            }
            .addOnFailureListener { e ->
                Log.w("PostRepositoryImpl", "Error adding document", e)
            }
    }

    fun deletePost(postId: String) {
        db.collection("posts").document(postId).delete()
            .addOnSuccessListener {
                // Log.d("PostRepositoryImpl", "DocumentSnapshot deleted!")
            }
            .addOnFailureListener { e ->
                // Log.w("PostRepositoryImpl", "Error deleting document", e)
            }
    }
}