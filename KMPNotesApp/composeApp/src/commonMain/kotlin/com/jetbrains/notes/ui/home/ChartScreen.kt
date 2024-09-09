package com.jetbrains.notes.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData

@Composable
fun ChartScreen(navController: NavHostController, homeViewModel: HomeViewModel) {
    Scaffold(
        bottomBar = {
//            AppBottomBar(
//                items = listOf(BottomNavItem.Home, BottomNavItem.Task),
//                navController = navController
//            )
            BottomNavigation(
                items = listOf(BottomNavItem.Home, BottomNavItem.Task, BottomNavItem.Chart),
                navController = navController,
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DonutChartSample()
        }
    }
}

@Composable
fun DonutChartSample() {

    val testPieChartData: List<PieChartData> = listOf(
        PieChartData(
            partName = "part A",
            data = 500.0,
            color = Color(0xFF0B666A),
        ),
        PieChartData(
            partName = "Part B",
            data = 700.0,
            color = Color(0xFF35A29F),
        ),
        PieChartData(
            partName = "Part C",
            data = 500.0,
            color = Color(0xFF97FEED),
        ),
        PieChartData(
            partName = "Part D",
            data = 100.0,
            color = Color(0xFF071952),
        ),
    )

    DonutChart(
        modifier = Modifier.fillMaxSize(),
        pieChartData = testPieChartData,
        centerTitle = "Orders",
        centerTitleStyle = TextStyle(color = Color(0xFF071952)),
        outerCircularColor = Color.LightGray,
        innerCircularColor = Color.Gray,
        ratioLineColor = Color.LightGray,
    )
}