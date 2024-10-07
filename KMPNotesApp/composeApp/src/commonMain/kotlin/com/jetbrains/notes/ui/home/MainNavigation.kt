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
import com.jetbrains.notes.ui.auth.AuthServiceImpl
import com.jetbrains.notes.ui.auth.LoginScreen1
import com.jetbrains.notes.ui.auth.SignUp
import com.jetbrains.notes.ui.taskScreen.TaskScreen
import com.jetbrains.notes.ui.taskdetails.TaskDetails
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.permissions.PermissionsController

@Composable
fun MainNavigation(dao: NotesDao, notificationScheduler: NotificationScheduler, permissionsController: PermissionsController) {
    val navController = rememberNavController()
    val startDestination = BottomNavItem.Login.route

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
        val homeViewModel = HomeViewModel(dao, permissionsController,AuthServiceImpl(Firebase.auth))
        composable(BottomNavItem.Login.route) {
            LoginScreen1(onLoginSuccess = {},navController)
        }
        composable(BottomNavItem.Signup.route) {
            SignUp(onLoginSuccess = {},navController)
        }

        composable(BottomNavItem.Home.route) {
            HomeScreen(
                navController = navController,
                modifier = Modifier,
                dao = dao,
//                viewModel = HomeViewModel(dao, permissionsController = permissionsController),
                viewModel2 = homeViewModel,
                onNavigate = {
                    navController.navigate(it.route)
                }

            )
        }
        composable(BottomNavItem.Task.route) {
            TaskScreen(navController, dao, homeViewModel)
        }
        composable(BottomNavItem.TaskDetails.route) {
            val taskId = it.arguments?.getString("id") ?: ""
            TaskDetails(modifier = Modifier, dao = dao, navController = navController,taskId, viewModel = homeViewModel)
        }
        composable(BottomNavItem.Chart.route) {
            ChartScreen(
                navController,
                homeViewModel
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Login : BottomNavItem("login", Icons.Default.Person, "Login")
    object Signup : BottomNavItem("signup", Icons.Default.Person, "Signup")
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
    object ProductDetails : BottomNavItem("productDetails", Icons.Default.Person, "Product Details")
    object Task : BottomNavItem("task", Icons.Default.Create, "Task")
    object TaskDetails : BottomNavItem("taskDetail/{id}", Icons.Default.Create, "Task Details")
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