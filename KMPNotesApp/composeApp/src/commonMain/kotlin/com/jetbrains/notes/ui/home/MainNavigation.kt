package com.jetbrains.notes.ui.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetbrains.notes.data.core.NotificationScheduler
import com.jetbrains.notes.data.model.local.NotesDao
import com.jetbrains.notes.ui.taskScreen.TaskScreen
import dev.icerock.moko.permissions.PermissionsController

@Composable
fun MainNavigation(dao: NotesDao, notificationScheduler: NotificationScheduler, permissionsController: PermissionsController) {
    val navController = rememberNavController()
    val startDestination = BottomNavItem.Home.route

    NavHost(navController = navController, startDestination = startDestination, enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
        )
    }, exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
        )
    }, popEnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
        )
    }, popExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
        )
    }) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                navController = navController,
                modifier = Modifier,
                dao = dao,
//                viewModel = HomeViewModel(dao, permissionsController = permissionsController),
                notificationScheduler = notificationScheduler,
                permissionsController = permissionsController
            )
        }
        composable(BottomNavItem.Task.route) {
            TaskScreen(navController, dao, HomeViewModel(dao, permissionsController = permissionsController))
        }
        composable(BottomNavItem.Chart.route) {
            ChartScreen(
                navController,
                HomeViewModel(dao, permissionsController = permissionsController)
            )
        }
//        composable(BottomNavItem.Profile.route) {
//            ProfileScreen(dao)
//        }
//        composable(
//            route = "${BottomNavItem.ProductDetails.route}/{productId}",
//            arguments = listOf(navArgument("productId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val productId = backStackEntry.arguments?.getString("productId")
//            if (productId != null) {
//                ProductDetailsScreen(productId, navController,dao)
//            }
//        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
    object ProductDetails : BottomNavItem("productDetails", Icons.Default.Person, "Product Details")
    object Task : BottomNavItem("task", Icons.Default.Create, "Task")
    object Chart : BottomNavItem("chart", Icons.Default.BarChart, "Chart")
}

@Composable
fun BottomNavigation(
    items: List<BottomNavItem>, navController: NavController, modifier: Modifier = Modifier
) {

    NavigationBar(modifier = modifier.fillMaxWidth().shadow(elevation = 8.dp)) {
        items.forEach { item ->
            NavigationBarItem(icon = {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title
                )
            },
                label = { Text(text = item.title) },
                selected = navController.currentBackStackEntry?.destination?.navigatorName == item.route,
                onClick = {
                    println("Item Route : ${item.route}")
//                    navigateBottomBar(navController,item.route)
                    navController.navigate(item.route)
                })
        }
    }
}