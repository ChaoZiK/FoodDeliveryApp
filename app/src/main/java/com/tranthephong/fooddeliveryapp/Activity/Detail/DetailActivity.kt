package com.tranthephong.fooddeliveryapp.Activity.Detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tranthephong.fooddeliveryapp.Activity.BaseActivity
import com.tranthephong.fooddeliveryapp.Activity.Cart.CartActivity
import com.tranthephong.fooddeliveryapp.Helper.ManagementCart
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.R
import com.tranthephong.fooddeliveryapp.Helper.TinyDB

class DetailActivity : BaseActivity() {
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagementCart
    private val TAG = "DetailActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = intent.getSerializableExtra("object") as ItemsModel
        Log.d(TAG, "DetailActivity created with item: ${item.title}")
        managementCart = ManagementCart(this)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance(
            "https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app"
        )

        val item = intent.getSerializableExtra("object") as ItemsModel

        setContent {
            DetailScreen(
                item=item,
                onBackClick = {finish()},
                onAddToCartClick = {
                    Log.d(TAG, "Add to cart clicked for item: ${item.title}")
                    item.numberInCart=1
                    managementCart.insertItems(item)
                },
                onCartClick = {
                    startActivity(Intent(this, CartActivity::class.java))
                },
                onFavoriteClick = { itemToFavorite, shouldBeFavorite ->
                    val user = auth.currentUser
                    if (user == null) {
                        Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
                        return@DetailScreen
                    }

                    val ref = database
                        .getReference("Users/${user.uid}/Favorites/${itemToFavorite.id}")

                    if (shouldBeFavorite) {
                        ref.setValue(itemToFavorite).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to add favorite", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        ref.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to remove favorite", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun DetailScreen(
    item: ItemsModel,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onCartClick: () -> Unit,
    onFavoriteClick: (ItemsModel, Boolean) -> Unit
) {
    var selectedImageUrl by remember { mutableStateOf(item.picUrl.first()) }
    var selectedModelIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance(
        "https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app"
    )
    var isFavorite by remember { mutableStateOf(false) }

//    LaunchedEffect(user) {
//        if (user != null) {
//            val ref = database.getReference("Users/${user.uid}/Favorites/${item.id}")
//            ref.get().addOnSuccessListener { snapshot ->
//                isFavorite = snapshot.exists()
//            }.addOnFailureListener {
//                Toast.makeText(context, "Failed to load favorite status", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
    LaunchedEffect(user, item.id) {
        isFavorite = false    // reset state immediately
        if (user != null && item.id.isNotBlank()) {
            val ref = database.getReference("Users/${user.uid}/Favorites/${item.id}")
            ref.get()
                .addOnSuccessListener { snapshot ->
                    isFavorite = snapshot.exists()
                }
                .addOnFailureListener {
                    // optional: log or toast
                }
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
                val newFavoriteState = !isFavorite
                isFavorite = newFavoriteState
                onFavoriteClick(item, newFavoriteState)
            }
        )

        InfoSection(item)

        ModelSelector(models = item.category,
            selectedModelIndex = selectedModelIndex,
            onModelSelected = {selectedModelIndex = it}
        )

        Text(text=item.description,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        FooterSection(onAddToCartClick, onCartClick)
    }
}

@Composable
private fun HeaderSection(
    selectedImageUrl: String,
    imageUrls:List<String>,
    onBackClick: () -> Unit,
    onImageSelected: (String) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
){

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .height(430.dp)
            .padding(bottom = 16.dp)
    ){
        val (back, fav, mainImage, thumbnail) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(model = selectedImageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.lightGrey),
                    shape = RoundedCornerShape(8.dp)
                )
                .constrainAs(mainImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        )
        BackButton(onBackClick, Modifier.constrainAs(back){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        })
//        FavoriteButton(Modifier.constrainAs(fav){
//            top.linkTo(parent.top)
//            end.linkTo(parent.end)
//        })
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


        LazyRow (modifier = Modifier
            .padding(vertical = 16.dp)
            .background(color = colorResource(R.color.white)
            , shape = RoundedCornerShape(10.dp)
            )
            .constrainAs(thumbnail) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
        ) {
            items(imageUrls.size) { index ->
                ImageThumbnail(
                    imageUrls[index],
                    isSelected = selectedImageUrl == imageUrls[index],
                    onClick = { onImageSelected(imageUrls[index]) }
                )
            }
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.back_arrow),
        contentDescription = null,
        modifier = modifier
            .padding(start = 16.dp, top = 48.dp)
            .clickable { onClick() }
    )
}

@Composable
private fun FavoriteButton(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.favorite),
        contentDescription = null,
        modifier = modifier
            .padding( top = 48.dp, end = 16.dp)
    )
}