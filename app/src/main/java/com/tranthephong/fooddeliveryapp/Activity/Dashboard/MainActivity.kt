package com.tranthephong.fooddeliveryapp.Activity.Dashboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tranthephong.fooddeliveryapp.Activity.BaseActivity
import com.tranthephong.fooddeliveryapp.Model.CategoryModel
import com.tranthephong.fooddeliveryapp.Model.ItemModel
import com.tranthephong.fooddeliveryapp.Model.SliderModel
import com.tranthephong.fooddeliveryapp.R
import com.tranthephong.fooddeliveryapp.ViewModel.MainViewModel
import com.tranthephong.fooddeliveryapp.components.SearchBar
import com.tranthephong.fooddeliveryapp.ui.theme.FoodDeliveryAppTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DashBoardScreen {

            }
        }
    }
}

@Composable
@Preview
fun DashBoardScreen(onCartClick: () -> Unit = {}){
    val viewModel: MainViewModel = viewModel()

    val banners = remember { mutableStateListOf<SliderModel>() }
    val categories = remember { mutableStateListOf<CategoryModel>() }
    val items = remember { mutableStateListOf<ItemModel>() }

    var showBannerLoading by remember { mutableStateOf(true) }
    var showCategoryLoading by remember { mutableStateOf(true) }
    var showItemLoading by remember { mutableStateOf(true) }

    val lifecycleOwner = LocalLifecycleOwner.current
    var searchText by remember { mutableStateOf("") }

    // banner
    LaunchedEffect(Unit) {
        viewModel.loadBanner().observeForever {
            banners.clear()
            banners.addAll(it)
            showBannerLoading = false
        }

        viewModel.loadCategory().observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading = false
        }

        viewModel.loadItem().observeForever {
            items.clear()
            items.addAll(it)
            showItemLoading = false
        }
    }

    ConstraintLayout(modifier = Modifier.background(Color.White)){
        val (scrollist,bottomMenu)=createRefs()

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .constrainAs(scrollist) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }){
            item{
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)
                    .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column {
                        Text("Welcome back", color = Color.Black)
                        Text("ChaoSiK", color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Banners
            item {
                if(showBannerLoading){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                } else {
                    Banners(banners)
                }
            }

            // Searh bar
            item {
                SearchBar(query = searchText, onSearchClick = {}, onQueryChange = { searchText = it })
            }

            // Categories
            item{
                if(showCategoryLoading){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                } else {
                    CategoryList(categories)
                }
            }

//            item {
//                Row (modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 24.dp)
//                    .padding(horizontal = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ){
//                    Text(
//                        text=
//                    )
//                }
//            }

            item {
                if(showItemLoading){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                } else {
                    ListItems(items)
                }
            }
        }

        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomMenu) {
                    bottom.linkTo(parent.bottom)
                },
            onItemClick = onCartClick
        )
    }
}