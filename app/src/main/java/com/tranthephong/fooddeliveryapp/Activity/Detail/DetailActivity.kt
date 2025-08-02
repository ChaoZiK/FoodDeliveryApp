package com.tranthephong.fooddeliveryapp.Activity.Detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tranthephong.fooddeliveryapp.Helper.ManagementCart
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.R
import com.tranthephong.fooddeliveryapp.navigation.Screen

@Composable
fun DetailScreenWrapper(
    navController: NavController,
    item: ItemsModel
) {
    val context = LocalContext.current
    val managementCart by remember { mutableStateOf(ManagementCart(context)) }
    val auth by remember { mutableStateOf(FirebaseAuth.getInstance()) }
    val database by remember {
        mutableStateOf(
            FirebaseDatabase.getInstance(
                "https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app"
            )
        )
    }

    DetailScreen(
        item = item,
        onBackClick = {
            navController.popBackStack(
                route = Screen.Home.route,
                inclusive = false
            )
        },
        onAddToCartClick = {
            item.numberInCart = 1
            managementCart.insertItems(item)
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
        },
        onCartClick = {
            navController.navigate("cart")
        },
        onFavoriteClick = { itemToFav, shouldBeFav ->
            val user = auth.currentUser
            if (user == null) {
                Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show()
                return@DetailScreen
            }
            val ref = database.getReference("Users/${user.uid}/Favorites/${itemToFav.id}")
            if (shouldBeFav) {
                ref.setValue(itemToFav)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to add favorite", Toast.LENGTH_SHORT).show()
                    }
            } else {
                ref.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to remove favorite", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    )
}

@Composable
private fun DetailScreen(
    item: ItemsModel,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onCartClick: () -> Unit,
    onFavoriteClick: (ItemsModel, Boolean) -> Unit
) {
    var selectedImageUrl by remember { mutableStateOf(item.picUrl.firstOrNull() ?: "") }
    var selectedModelIndex by remember { mutableStateOf(-1) }
    var isFavorite by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance(
        "https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app"
    )

    LaunchedEffect(auth.currentUser, item.id) {
        isFavorite = false
        auth.currentUser?.let { user ->
            val favRef = database.getReference("Users/${user.uid}/Favorites/${item.id}")
            favRef.get()
                .addOnSuccessListener { snap -> isFavorite = snap.exists() }
                .addOnFailureListener {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(
            selectedImageUrl = selectedImageUrl,
            imageUrls = item.picUrl,
            onBackClick = onBackClick,
            onImageSelected = { selectedImageUrl = it },
            isFavorite = isFavorite,
            onFavoriteClick = {
                val newState = !isFavorite
                isFavorite = newState
                onFavoriteClick(item, newState)
            }
        )

        InfoSection(item = item)

        Spacer(modifier = Modifier.height(24.dp))

        ModelSelector(
            models = item.category,
            selectedModelIndex = selectedModelIndex,
            onModelSelected = { selectedModelIndex = it }
        )

        Text(
            text = item.description,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        FooterSection(
            onAddToCartClick = onAddToCartClick,
            onCartClick = onCartClick
        )
    }
}

@Composable
private fun HeaderSection(
    selectedImageUrl: String,
    imageUrls: List<String>,
    onBackClick: () -> Unit,
    onImageSelected: (String) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .height(430.dp)
            .padding(bottom = 16.dp)
    ) {
        val (back, fav, mainImage) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(model = selectedImageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    colorResource(R.color.lightGrey),
                    shape = RoundedCornerShape(8.dp)
                )
                .constrainAs(mainImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        )
        Box(
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .size(36.dp)
                .background(Color.White, shape = CircleShape)
                .clickable { onBackClick() }
                .constrainAs(back) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Back",
                modifier = Modifier.size(20.dp)
            )
        }
        val heartIcon = if (isFavorite) R.drawable.fbutton_on else R.drawable.fbutton_off
        Image(
            painter = painterResource(id = heartIcon),
            contentDescription = "Favorite",
            modifier = Modifier
                .padding(top = 48.dp, end = 16.dp)
                .clickable { onFavoriteClick() }
                .constrainAs(fav) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )
    }
}
