package com.example.travel.components.ProfileComponents


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.travel.R
import com.example.travel.data.Achievement.Companion.achievements
import com.example.travel.data.User


val achievementImages = listOf(
    R.drawable.achievement1,
    R.drawable.achievement2,
    R.drawable.achievement3,
    R.drawable.achievement4
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun AchievementsLayout(userInfo: User? = null) {

    val zoomStates = remember { mutableStateListOf<Boolean>().apply { repeat(achievements.size) { add(false) } } }
    var fullScreenImage: Int? by remember { mutableStateOf(null) }

    val pagerState = rememberPagerState(){2}
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
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(10.dp)
            )
            HorizontalPager(
                state = pagerState,
                key = { achievementImages[it] },
                pageSize = PageSize.Fill
            ) { index ->
                AchievementItem(
                    achievementImage = painterResource(id = achievementImages[3 * index]),
                    onClick = {
                        fullScreenImage = 3 * index
                        zoomStates.fill(false)
                        zoomStates[3 * index] = true },
                    index = 3 * index,
                    userInfo = userInfo
                )
                if (3 * index + 1 >= achievementImages.size) {
                    return@HorizontalPager
                }
                AchievementItem(
                    achievementImage = painterResource(id = achievementImages[3 * index + 1]),
                    onClick = {
                        fullScreenImage = 3 * index + 1
                        zoomStates.fill(false)
                        zoomStates[3 * index + 1] = true },
                    index = 3 * index + 1,
                    userInfo = userInfo
                )

                if (3 * index + 2 >= achievementImages.size) {
                    return@HorizontalPager
                }
                AchievementItem(
                    achievementImage = painterResource(id = achievementImages[3 * index + 2]),
                    onClick = {
                        fullScreenImage = 3 * index + 2
                        zoomStates.fill(false)
                        zoomStates[3 * index + 2] = true},
                    userInfo = userInfo,
                    index = 3 * index + 2
                )
            }

            fullScreenImage?.let {image ->
                FullScreenImageDialog(image, onDismiss = { fullScreenImage = null }, userInfo = userInfo)
            }
        }
    }
}
@Composable
fun AchievementItem(
    modifier: Modifier = Modifier,
    achievementImage: Painter,
    onClick: () -> Unit,
    userInfo: User? = null,
    index: Int
) {
    Spacer(modifier = Modifier.width(15.dp))
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = achievementImage,
            contentDescription = null,
            colorFilter = if (userInfo != null && userInfo.achievements.contains(achievements[index].title)) null else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .clip(CircleShape)
                .alpha(if (userInfo != null && userInfo.achievements.contains(achievements[index].title)) 1f else 0.4f)
        )
    }
}

@Composable
fun FullScreenImageDialog(
    currentIndex: Int,
    onDismiss: () -> Unit,
    userInfo: User? = null
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(color = Color.Black.copy(alpha = 0.01f)) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(450.dp)
                    .wrapContentSize()
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Column (Modifier.padding(10.dp)) {
                    Text(
                        text = achievements[currentIndex].title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally)
                    )
                    Image(
                        painter = painterResource(id = achievementImages[currentIndex]),
                        contentDescription = null,
                        colorFilter = if (userInfo != null && userInfo.achievements.contains(achievements[currentIndex].title)) null else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                        modifier = Modifier.clip(CircleShape).align(Alignment.CenterHorizontally).height(200.dp).width(200.dp).alpha(if (userInfo != null && userInfo.achievements.contains(achievements[currentIndex].title)) 1f else 0.7f)

                    )
                    Text(
                        text = if (userInfo != null && userInfo.achievements.contains(achievements[currentIndex].title)) achievements[currentIndex].description else achievements[currentIndex].requirement,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}