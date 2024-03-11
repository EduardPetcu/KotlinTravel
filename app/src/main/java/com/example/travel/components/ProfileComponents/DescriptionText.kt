package com.example.travel.components.ProfileComponents

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.travel.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun DescriptionText(userInfo: User? = null, context: Context) {
    val context = LocalContext.current
    var isEditingBio by remember { mutableStateOf(false) }
    var textBioStart = userInfo?.userBio
    var textBio by remember { mutableStateOf(textBioStart ?: "") }
    Surface(
        shape = RoundedCornerShape(8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = Color(0xFFD5C28C)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "About me",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.width(5.dp))
                if (isEditingBio) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "save bio",
                        tint = Color.Blue,
                        modifier = Modifier
                            .clickable {
                                isEditingBio = false
                                updateUserBioInFirebase(textBio!!, context)
                                userInfo?.userBio = textBio
                                textBioStart = textBio
                            }

                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit description",
                        tint = Color.Blue,
                        modifier = Modifier
                            .clickable {
                                isEditingBio = true
                                textBio = textBioStart ?: ""
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            if (isEditingBio) {
                TextField(value = textBio!!,
                    onValueChange = { text : String -> textBio = text },
                    textStyle = MaterialTheme.typography.h6,
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFD5C28C)),
                    modifier = Modifier.fillMaxWidth())
                Button(onClick = {
                    isEditingBio = false
                    updateUserBioInFirebase(textBio!!, context)
                    userInfo?.userBio = textBio
                    textBioStart = textBio
                }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("Save")
                }
            }
            else {
                Text(
                    text = userInfo?.userBio ?: "Loading...",
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

fun updateUserBioInFirebase(userBio: String, context: Context) {
    val user = FirebaseAuth.getInstance().currentUser!!.uid
    val db = Firebase.firestore
    db.collection("users").document(user).update("userBio", userBio)
        .addOnSuccessListener {
            Log.d("ProfileScreen", "User bio updated")
            Toast.makeText(
                context,
                "User bio updated",
                Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener {
            Log.e("ProfileScreen", "User bio update failed", it)
        }
}