package com.tranthephong.fooddeliveryapp.Activity.Detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = intent.getSerializableExtra("object") as ItemsModel
        Log.d(TAG, "DetailActivity created with item: ${item.title}")
        managementCart = ManagementCart(this)

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
    onCartClick: () -> Unit
) {
    var selectedImageUrl by remember { mutableStateOf(item.picUrl.first()) }
    var selectedModelIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(selectedImageUrl,item.picUrl,onBackClick) {selectedImageUrl = it }

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
    onImageSelected: (String) -> Unit
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
        FavoriteButton(Modifier.constrainAs(fav){
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        })


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