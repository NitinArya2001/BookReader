package com.example.bookreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookreader.screens.ReaderSplashScreen
import com.example.bookreader.screens.details.BookDetailsScreen
import com.example.bookreader.screens.home.HomeScreen
import com.example.bookreader.screens.home.HomeScreenViewModel
import com.example.bookreader.screens.login.ReaderLoginScreen
import com.example.bookreader.screens.search.SearchScreen
import com.example.bookreader.screens.stats.ReaderStatsScreen
import com.example.bookreader.screens.update.BookUpdateScreen


@Composable
fun ReaderNavigation() {

    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ReaderScreens.SplashScreen.name) {

        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreens.HomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = homeViewModel)

        }

        composable(ReaderScreens.ReaderStatsScreen.name){
            val viewModel: HomeScreenViewModel = hiltViewModel()
            ReaderStatsScreen(navController = navController,viewModel)
        }

        composable(ReaderScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }

        val detailName = ReaderScreens.DetailsScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){ backStacKEntry ->
            backStacKEntry.arguments?.getString("bookId").let{
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }

        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable(
            "$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId"){
                type = NavType.StringType
            })){ navBackStackEntry ->
                navBackStackEntry.arguments?.getString("bookItemId").let {
                    BookUpdateScreen(navController = navController,bookItemId = it.toString())
                }
        }

    }
}