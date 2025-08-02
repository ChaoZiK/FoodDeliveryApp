package com.tranthephong.fooddeliveryapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.tranthephong.fooddeliveryapp.Activity.Authentication.LoginActivity
import com.tranthephong.fooddeliveryapp.Activity.Cart.CartScreen
import com.tranthephong.fooddeliveryapp.Activity.Cart.ConfirmationScreen
import com.tranthephong.fooddeliveryapp.Activity.Detail.DetailScreenWrapper
import com.tranthephong.fooddeliveryapp.Activity.Favorite.FavoriteScreen
import com.tranthephong.fooddeliveryapp.Activity.Home.DashBoardScreen
import com.tranthephong.fooddeliveryapp.Activity.Profile.AccountInfoScreen
import com.tranthephong.fooddeliveryapp.Activity.Profile.ChangeNameScreen
import com.tranthephong.fooddeliveryapp.Activity.Profile.ChangePasswordScreen
import com.tranthephong.fooddeliveryapp.Activity.Profile.OrderDetail
import com.tranthephong.fooddeliveryapp.Activity.Profile.Orders
import com.tranthephong.fooddeliveryapp.Activity.Profile.ProfileScreen
import com.tranthephong.fooddeliveryapp.Helper.ManagementCart
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.Model.Order
import com.tranthephong.fooddeliveryapp.Util.RememberPrefs
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
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
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
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        composable(Screen.Home.route) {
                            DashBoardScreen(
                                onItemClick = { item ->
                                    val json = Uri.encode(Gson().toJson(item))
                                    navController.navigate("detail/$json")
                                },
                                onCartClick = { navController.navigate(Screen.Cart.route) },
                                onFavoriteClick = { navController.navigate(Screen.Favorites.route) },
                                onProfileClick = { navController.navigate(Screen.Profile.route) },
                                navController,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Favorites.route) {
                            FavoriteScreen(
                                onItemClick = { selectedItem ->
                                    val json = Uri.encode(Gson().toJson(selectedItem))
                                    navController.navigate("detail/$json")
                                }
                            )
                        }
                        composable(Screen.Cart.route) {
                            val ctx = LocalContext.current
                            val cartHelper = remember { ManagementCart(ctx) }

                            val onItemChange = {
                            }

                            val onCheckout = {
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user == null) {
                                    Toast.makeText(ctx, "Please log in first", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    val itemTotal = cartHelper.getTotalFee()
                                    val tax = (itemTotal * 0.02).let { (it * 100).toInt() / 100.0 }
                                    val delivery = 10.0
                                    val totalPrice = itemTotal + tax + delivery

                                    val fullItems: List<ItemsModel> = cartHelper.getListCart()

                                    val ordersRef = FirebaseDatabase
                                        .getInstance("https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app")
                                        .getReference("Users/${user.uid}/Orders")
                                        .push()
                                    val orderId =
                                        ordersRef.key ?: System.currentTimeMillis().toString()
                                    val order = Order(
                                        orderId,
                                        fullItems,
                                        totalPrice,
                                        System.currentTimeMillis()
                                    )

                                    ordersRef.setValue(order)
                                        .addOnSuccessListener {
                                            cartHelper.clearCart()
                                            onItemChange()
                                            navController.navigate("confirmation")
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                ctx,
                                                "Order failed: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                                Unit
                            }


                            CartScreen(
                                onItemChange = onItemChange,
                                onCheckout = onCheckout
                            )

                        }
                        composable(Screen.Profile.route) {
                            val ctx = LocalContext.current
                            ProfileScreen(
                                onHomeClick = { navController.navigate(Screen.Home.route) },
                                onCartClick = { navController.navigate(Screen.Cart.route) },
                                onFavoriteClick = { navController.navigate(Screen.Favorites.route) },
                                onOrdersClick = { navController.navigate("orders") },
                                onLogoutClick = {
                                    FirebaseAuth.getInstance().signOut()
                                    RememberPrefs.setRemember(ctx, false)
                                    ctx.startActivity(Intent(ctx, LoginActivity::class.java))
                                    finish()
                                },
                                onAccountInfoClick = { navController.navigate("accountInfo") }
                            )
                        }

                        composable("confirmation") {
                            ConfirmationScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            "detail/{itemJson}",
                            arguments = listOf(navArgument("itemJson") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val raw = backStackEntry.arguments?.getString("itemJson")
                            if (raw == null) {
                                navController.popBackStack()
                                return@composable
                            }

                            val decodedJson = Uri.decode(raw)

                            val item = Gson().fromJson(decodedJson, ItemsModel::class.java)

                            DetailScreenWrapper(navController, item)
                        }
                        composable("orders") {
                            Orders(
                                onBack = { navController.popBackStack() },
                                onOrderClick = { orderJson ->
                                    navController.navigate("orderDetail/${Uri.encode(orderJson)}")
                                }
                            )
                        }
                        composable(
                            "orderDetail/{orderJson}",
                            arguments = listOf(navArgument("orderJson") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val rawJson = backStackEntry.arguments?.getString("orderJson")!!
                            val order = Gson().fromJson(Uri.decode(rawJson), Order::class.java)
                            OrderDetail(
                                order = order,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("accountInfo") {
                            val user = FirebaseAuth.getInstance().currentUser!!
                            AccountInfoScreen(
                                nav = navController,
                                email = user.email ?: "",
                                name = user.displayName ?: ""
                            )
                        }
                        composable("changeName") {
                            val user = FirebaseAuth.getInstance().currentUser!!
                            ChangeNameScreen(navController, user.displayName ?: "")
                        }
                        composable("changePassword") {
                            val user = FirebaseAuth.getInstance().currentUser!!
                            ChangePasswordScreen(navController, user.email ?: "")
                        }
                    }
                }
            }
        }
    }
}
