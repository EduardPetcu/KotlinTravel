package com.example.travel.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travel.R
import com.example.travel.navigation.Screen
import com.example.travel.navigation.TravelAppRouter
import com.example.travel.screens.CalculateScreen
import com.example.travel.screens.HomeScreen
import com.example.travel.screens.ProfileScreen
import com.example.travel.screens.TransportScreen
import com.google.android.material.tabs.TabLayout

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
            contentDescription = title
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
    object homeTab : TabBarItem("home", Icons.Filled.Home, Icons.Outlined.Home, false)
    object calculteTab : TabBarItem("calculate", Icons.Filled.Calculate, Icons.Outlined.Calculate, false)
    object transportTab : TabBarItem("transport", Icons.Filled.Train, Icons.Outlined.Train, false)
    object profileTab : TabBarItem("profile", Icons.Filled.Person, Icons.Outlined.Person, false)



//    val calculateTab = TabBarItem(
//        title = "calculate",
//        selectedIcon = Icons.Filled.Calculate,
//        unselectedIcon = Icons.Outlined.Calculate
//    )
//    val transportTab = TabBarItem(
//        title = "transport",
//        selectedIcon = Icons.Filled.Train,
//        unselectedIcon = Icons.Outlined.Train
//    )
//    val profileTab = TabBarItem(
//        title = "profile",
//        selectedIcon = Icons.Filled.Person,
//        unselectedIcon = Icons.Outlined.Person
//    )
}
@Composable
fun TabView(tabBarItems: List<TabBarItem>) {
    var selectedTabIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    Log.d("Component Tab View", "Navigating to ${tabBarItem.title}")
                    Log.d("Component Tab View", "Index is $index")
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
