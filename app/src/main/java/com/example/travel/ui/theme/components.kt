package com.example.travel.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.data.User
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.travel.components.RenderPicture
import com.example.travel.repository.DatabaseRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

open class InputType(
    val label: String,
    val icon: ImageVector?,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Name : InputType(
        label = "Username",
        icon = Icons.Default.Person,
        KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        label = "Password",
        icon = Icons.Default.Lock,
        KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )

    object Email : InputType(
        label = "Email",
        icon = Icons.Default.Email,
        KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
        visualTransformation = VisualTransformation.None
    )

    object BudgetName: InputType(
        label = "Budget name",
        icon = Icons.Default.Wallet,
        KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
        visualTransformation = VisualTransformation.None
    )

    object BudgetAmount: InputType(
        label = "Budget amount",
        icon = Icons.Default.MonetizationOn,
        KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
        visualTransformation = VisualTransformation.None
    )
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val selected_icon: ImageVector,val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, Icons.Filled.Home, "Home")
    object Calculator : BottomNavItem("calculate", Icons.Default.Calculate,Icons.Filled.Calculate, "Calculate")
    object Map : BottomNavItem("map", Icons.Default.Map, Icons.Filled.Map,"Map")
    object Profile : BottomNavItem("profile", Icons.Default.Person, Icons.Filled.Person,"Profile")
}
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title,
            tint = if (isSelected) {
                Color(251, 242, 33)
            } else {
                Color.Gray
            }
        )
    }
}

sealed class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeAmount: Int? = null
) {
    object homeTab : TabBarItem("Home", Icons.Filled.Home, Icons.Outlined.Home, false)
    object calculteTab : TabBarItem("Calculate", Icons.Filled.Calculate, Icons.Outlined.Calculate, false)
    object mapTab : TabBarItem("Map", Icons.Filled.Map, Icons.Outlined.Map, false)
    object profileTab : TabBarItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, false)
}
@Composable
fun TabView(tabBarItems: List<TabBarItem>, selectedTabIndex: Int) {

    NavigationBar (
        containerColor = BackgroundBlue,
        ) {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Green,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Yellow,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = BackgroundBlue
                ),
                selected = selectedTabIndex == index,
                onClick = {
                    Log.d("Component Tab View", "Navigating to ${tabBarItem.title}")
                    Log.d("Component Tab View", "Index is $index and selectedTabIndex is $selectedTabIndex");
                    when (index) {
                        0 -> TravelAppRouter.navigateTo(Screen.HomeScreen)
                        1 -> TravelAppRouter.navigateTo(Screen.CalculateScreen)
                        2 -> TravelAppRouter.navigateTo(Screen.TransportScreen)
                        3 -> TravelAppRouter.navigateTo(Screen.ProfileScreen)
                    }
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                    )
                },
                label = {Text(tabBarItem.title)})
        }
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}
@Composable
fun UserProfile(modifier: Modifier = Modifier, databaseRepositoryImpl: DatabaseRepositoryImpl) {
    var userInfo by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(key1 = true) {
        databaseRepositoryImpl.fetchUserData { user ->
            userInfo = user
        }
    }
    if (userInfo == null) {
        CircularProgressIndicator(modifier = Modifier.padding(25.dp))
    } else {
        ProfilePicture(userInfo = userInfo!!, modifier = modifier)
    }
}

@Composable
fun ProfilePicture(userInfo: User, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(16.dp)
    ) {
        // Display user image in a circle shape
        RenderPicture(true, userInfo)
        Column (
            modifier = Modifier
                .padding(start = 16.dp)
        ){
            Text(text = userInfo.username,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp))
            Text(text = userInfo.userRole,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp))
        }
    }
}