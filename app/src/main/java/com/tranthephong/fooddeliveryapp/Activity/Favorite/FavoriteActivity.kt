package com.tranthephong.fooddeliveryapp.Activity.Favorite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tranthephong.fooddeliveryapp.Activity.BaseActivity
import com.tranthephong.fooddeliveryapp.Activity.Dashboard.ListItemsFullSizeVertical
import com.tranthephong.fooddeliveryapp.Helper.TinyDB
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.R

class FavoriteActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FavoriteScreen(
                onBackClick = { finish() }
            )
        }
    }
}

@Composable
fun FavoriteScreen(
    onBackClick: () -> Unit
) {
    val tinyDB = TinyDB(LocalContext.current)
    var favoriteItems by remember { mutableStateOf<List<ItemsModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        favoriteItems = tinyDB.getListObject("favorites") ?: emptyList()
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)
        ) {
            val (backBtn, titleTxt) = createRefs()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(titleTxt) { centerTo(parent) },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                text = "Favorites"
            )

            Image(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (favoriteItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite items yet",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            ListItemsFullSizeVertical(favoriteItems)
        }
    }
}