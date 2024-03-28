package com.example.travel.components.ProfileComponents

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.travel.R
import com.example.travel.data.User


// TODO: Make a logic where achievements are fetched from the database
// TODO: and displayed in the HorizontalPager with alpha = 0.3f if not achieved
// TODO: Implement an onClick method on each achievement to display a dialog with the achievement details
@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun AchievementsLayout() {
    val achievementImages = listOf(
        R.drawable.achievement1,
        R.drawable.achievement2,
        R.drawable.achievement3,
        R.drawable.achievement4
    )
    val pagerState = rememberPagerState(){2};
    Surface(
        shape = RoundedCornerShape(8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = Color(0xFFD5C28C)
    ) {
        Column(modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp)) {
            Text(
                text = "Achievements",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(10.dp)
            )
            HorizontalPager(
                state = pagerState,
                key = { achievementImages[it] },
                pageSize = PageSize.Fill
            ) { index ->
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painter = painterResource(id = achievementImages[3 * index]),
                    contentDescription = "Achievement",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(100.dp)
                        .width(100.dp)
                )
                if (3 * index + 1 >= achievementImages.size) {
                    return@HorizontalPager
                }
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painter = painterResource(id = achievementImages[3 * index + 1]),
                    contentDescription = "Achievement",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(100.dp)
                        .width(100.dp),
                    alpha = 0.3f
                )
                if (3 * index + 2 >= achievementImages.size) {
                    return@HorizontalPager
                }
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painter = painterResource(id = achievementImages[3 * index + 2]),
                    contentDescription = "Achievement",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(100.dp)
                        .width(100.dp),
                    alpha = 0.3f
                )
            }
        }
    }
}