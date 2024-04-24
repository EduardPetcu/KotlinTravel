package com.example.travel.components.ProfileComponents

import android.content.Context
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.travel.components.RenderPicture
import com.example.travel.data.User
import com.example.travel.ui.theme.ContainerYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopProfileLayout(userInfo: User? = null, context: Context) {
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
                RenderPicture()
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
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