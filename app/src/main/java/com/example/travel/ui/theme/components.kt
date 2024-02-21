package com.example.travel.ui.theme

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter

// TODO: Make label text yellow as well.
// TODO: Remove redundant imports and code
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
    // change background color to blue

    NavigationBar (
        containerColor = Color.hsl(236f, 0.58f, 0.52f)
        ) {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
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
