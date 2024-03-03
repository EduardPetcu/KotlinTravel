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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Train
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
import androidx.compose.ui.unit.sp

// DONE: Make label text yellow as well.
sealed class InputType(
    val label: String,
    val icon: ImageVector,
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
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val selected_icon: ImageVector,val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, Icons.Filled.Home, "Home")
    object Calculator : BottomNavItem("calculate", Icons.Default.Calculate,Icons.Filled.Calculate, "Calculate")
    object Transports : BottomNavItem("transport", Icons.Default.Train, Icons.Filled.Train,"Transport")
    object Profile : BottomNavItem("profile", Icons.Default.Person, Icons.Filled.Person,"Profile")
}
@OptIn(ExperimentalMaterial3Api::class)
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
                androidx.compose.ui.graphics.Color(251, 242, 33)
            } else {
                androidx.compose.ui.graphics.Color.Gray
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
    object transportTab : TabBarItem("Transport", Icons.Filled.Train, Icons.Outlined.Train, false)
    object profileTab : TabBarItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, false)
}
@Composable
fun TabView(tabBarItems: List<TabBarItem>, selectedTabIndex: Int) {

    NavigationBar (
        containerColor = Color.hsl(236f, 0.58f, 0.52f),
        ) {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Green,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Yellow,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.hsl(236f, 0.58f, 0.52f)
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
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}
@Composable
fun UserProfile() {
    var userInfo by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(key1 = true) {
        fetchUserData { user ->
            userInfo = user
        }
    }

    ProfileScreen(userInfo = userInfo)
}

@Composable
fun ProfileScreen(userInfo: User?) {
    Row(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Display user image in a circle shape
        userInfo?.userImage?.let { imageUrl ->
            Image(
                painter = painterResource(id = R.drawable.standard_pfp),
                contentDescription = "User Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }
        Column (
            modifier = Modifier
                .padding(start = 16.dp)
        ){
            Text(text = userInfo?.username ?: "Loading...",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp))
            Text(text = userInfo?.userRole ?: "Loading...",
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp))
        }
    }
}

fun fetchUserData(onSuccess: (User) -> Unit) {
    val db = Firebase.firestore
    val userAuth = FirebaseAuth.getInstance().currentUser?.uid
    Log.d("ProfileAvatar", "User Auth: $userAuth");
    userAuth?.let {
        db.collection("users").document(it).get()
            .addOnSuccessListener { document ->
                val userReceived = document.toObject<User>()
                if (userReceived != null) {
                    onSuccess(userReceived)
                    Log.d("ProfileAvatar", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("ProfileAvatar", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ProfileAvatar", "get failed with ", exception)
            }
    }
}