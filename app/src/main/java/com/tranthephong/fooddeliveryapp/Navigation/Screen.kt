package com.tranthephong.fooddeliveryapp.navigation

import androidx.annotation.DrawableRes
import com.tranthephong.fooddeliveryapp.R

sealed class Screen(val route: String, @DrawableRes val icon: Int) {
    object Home : Screen("home", R.drawable.home)
    object Favorites : Screen("favorites", R.drawable.favorite)
    object Cart : Screen("cart", R.drawable.cart)
    object Profile : Screen("profile", R.drawable.profile)
}
