package com.example.travel.components.ProfileComponents

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.travel.components.RenderPicture
import com.example.travel.data.User
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopProfileLayout(userInfo: User, context: Context, isMe: Boolean, followerList: List<String>?) {
    val followerListUpdated = remember { mutableStateListOf<String>() }
    val databaseRepositoryImpl = DatabaseRepositoryImpl()
    LaunchedEffect(followerList) {
        followerListUpdated.addAll(followerList ?: emptyList())
    }
    Surface(
        shape = RoundedCornerShape(8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = ContainerYellow
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RenderPicture(isMe, userInfo)
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    if (!isMe) {
                        if (followerListUpdated.contains(userInfo.username)) {
                            Button(
                                onClick = {
                                    followerListUpdated.remove(userInfo.username)
                                    databaseRepositoryImpl.updateUserData(mapOf("followedUsers" to followerListUpdated))
                                    Toast.makeText(context, "Unfollowed ${userInfo.username}", Toast.LENGTH_SHORT).show()
                                          },
                                colors = ButtonDefaults.buttonColors(containerColor = BackgroundBlue)
                            ) {
                                Text(
                                    text = "Unfollow",
                                    style = MaterialTheme.typography.h6,
                                    color = Color.White
                                )
                            }
                        } else {
                            Button(
                                onClick = { followerListUpdated += userInfo.username
                                    databaseRepositoryImpl.updateUserData(mapOf("followedUsers" to followerListUpdated))
                                    Toast.makeText(context, "Followed ${userInfo.username}", Toast.LENGTH_SHORT).show()
                                    },
                                colors = ButtonDefaults.buttonColors(containerColor = BackgroundBlue)
                            ) {
                                Text(
                                    text = "Follow",
                                    style = MaterialTheme.typography.h6,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Text(
                        text = userInfo?.username ?: "Loading...",
                        style = MaterialTheme.typography.h5
                    )

                    Text(
                        text = userInfo?.userRole ?: "Loading...",
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            FlowRow(modifier = Modifier.padding(vertical = 5.dp)) {
                ImageTextContent(
                    modifier = Modifier.padding(vertical = 5.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "emaill",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            text = userInfo?.email ?: "Loading...",
                            style = MaterialTheme.typography.h5,
                        )
                    }
                )
                ImageTextContent(
                    modifier = Modifier.padding(vertical = 5.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "locationn",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            text = if (userInfo?.country != null && userInfo.city != null) {
                                userInfo.country + ", " + userInfo.city
                            } else {
                                "Loading..."
                            },
                            style = MaterialTheme.typography.h5,
                        )
                    }
                )
            }
        }

    }

}

@Composable
fun ImageTextContent(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(modifier = Modifier.width(5.dp))
        text()
        Spacer(modifier = Modifier.width(10.dp))
    }
}