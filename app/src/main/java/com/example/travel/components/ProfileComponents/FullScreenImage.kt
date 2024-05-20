package com.example.travel.components.ProfileComponents

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullScreenImage(
    uri: Uri,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d("FullScreenImage", "uri: $uri")
    Dialog(onDismissRequest = onDismiss) {
        Surface(color = Color.Black.copy(alpha = 0.01f),
            modifier = Modifier.clickable {
                onDismiss()
            }.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(450.dp)
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                Column (Modifier.padding(10.dp)) {
                    GlideImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .heightIn(max = 300.dp)
                            .widthIn(max = 300.dp)
                    )
                }
            }
        }
    }
}