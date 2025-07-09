package com.tranthephong.fooddeliveryapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.tranthephong.fooddeliveryapp.Activity.Cart.CartScreen
import com.tranthephong.fooddeliveryapp.Activity.Detail.DetailScreenWrapper
import com.tranthephong.fooddeliveryapp.Activity.Favorite.FavoriteScreen
import com.tranthephong.fooddeliveryapp.Activity.Home.DashBoardScreen
import com.tranthephong.fooddeliveryapp.Activity.Profile.ProfileScreen
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.navigation.Screen
import com.tranthephong.fooddeliveryapp.ui.theme.FoodDeliveryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodDeliveryAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    containerColor = Color.White,
                    bottomBar = {
                        val navBackStack by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStack?.destination?.route

                        NavigationBar {
                            listOf(
                                Screen.Home,
                                Screen.Favorites,
                                Screen.Cart,
                                Screen.Profile
                            ).forEach { screen ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            painterResource(screen.icon),
                                            contentDescription = null
                                        )
                                    },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        if (currentRoute != screen.route) {
                                            navController.navigate(screen.route) {
                                                // popUpTo start to avoid stacking duplicates
                                                popUpTo(navController.graph.findStartDestination().id) {
//                                                    saveState = true
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
//                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier
                            .fillMaxSize()              // <— must fill the scaffold’s content area
                            .padding(paddingValues)
                    ) {
                        composable(Screen.Home.route) {
                            DashBoardScreen(
//                                onItemClick = { item ->
//                                    navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
//                                    navController.navigate("detail")
//                                },
                                onItemClick = { item ->
                                    val json = Uri.encode(Gson().toJson(item))
                                    navController.navigate("detail/$json")
                                },
                                onCartClick = { navController.navigate(Screen.Cart.route) },
                                onFavoriteClick = { navController.navigate(Screen.Favorites.route) },
                                onProfileClick = { navController.navigate(Screen.Profile.route) }
                            )
                        }
                        composable(Screen.Favorites.route) {
                            FavoriteScreen(
//                                onItemClick = { selectedItem ->
//                                    navController.currentBackStackEntry
//                                        ?.savedStateHandle
//                                        ?.set("item", selectedItem)      // ← use the handle, not arguments
//                                    navController.navigate("detail")
//                                }
                                onItemClick = { selectedItem ->
                                    val json = Uri.encode(Gson().toJson(selectedItem))
                                    navController.navigate("detail/$json")
                                }
                            )
                        }
                        composable(Screen.Cart.route) {
                            CartScreen()
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                onHomeClick       = { navController.navigate(Screen.Home.route) },
                                onCartClick       = { navController.navigate(Screen.Cart.route) },
                                onFavoriteClick  = { navController.navigate(Screen.Favorites.route) }
                            )
                        }
//                        composable("detail") {
////                            val item = navController
////                                .previousBackStackEntry
////                                ?.savedStateHandle
////                                ?.get<ItemsModel>("item")
////
////                            // Defensive check
////                            if (item == null) {
////                                navController.popBackStack()
////                                return@composable
////                            }
//                                backStackEntry ->
//                            val item = backStackEntry.arguments
//                                ?.getSerializable("item")
//                                    as ItemsModel  // safe once you put it
//
//                            DetailScreenWrapper(navController, item)
//                        }
                        // 1) Add an argument to your NavHost
                        composable(
                            "detail/{itemJson}",
                            arguments = listOf(navArgument("itemJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // grab the raw (encoded) string
                            val raw = backStackEntry.arguments?.getString("itemJson")
                            if (raw == null) {
                                navController.popBackStack()
                                return@composable
                            }

                            // URL-decode it
                            val decodedJson = Uri.decode(raw)

                            // now deserialize
                            val item = Gson().fromJson(decodedJson, ItemsModel::class.java)

                            DetailScreenWrapper(navController, item)
                        }
                    }
            }
        }}
    }
}
