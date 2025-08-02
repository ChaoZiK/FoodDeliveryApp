package com.tranthephong.fooddeliveryapp.Activity.Home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.Model.SliderModel
import com.tranthephong.fooddeliveryapp.R
import com.tranthephong.fooddeliveryapp.ViewModel.MainViewModel
import com.tranthephong.fooddeliveryapp.components.SearchBar

@Composable
fun DashBoardScreen(
    onItemClick: (ItemsModel) -> Unit,
    onCartClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavController,
    onBackClick: () -> Unit
) {
    val viewModel: MainViewModel = viewModel()

    val banners = remember { mutableStateListOf<SliderModel>() }
    val items = remember { mutableStateListOf<ItemsModel>() }

    var showBannerLoading = banners.isEmpty()
    var showItemLoading = items.isEmpty()

    var searchText by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadBanner().observeForever {
            banners.clear()
            banners.addAll(it)
            showBannerLoading = false
        }

        viewModel.loadItem().observeForever {
            items.clear()
            items.addAll(it)
            showItemLoading = false
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (!searchActive) {
            val gridState = rememberLazyGridState()

            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item(span = { GridItemSpan(maxLineSpan) }) {
                    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Welcome back", color = Color.Black)
                            Text(
                                userName, color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    if (showBannerLoading) LoadingBox(200.dp) else Banners(banners)
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    SearchBar(
                        query = searchText,
                        onQueryChange = { searchText = it },
                        onSearchClick = { searchActive = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                            .onFocusChanged { if (it.isFocused) searchActive = true }
                    )
                }

                if (showItemLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingBox(200.dp)
                    }
                } else {
                    items(items.size) { idx ->
                        Item(
                            item = items[idx],
                            onClick = { onItemClick(items[idx]) }
                        )
                    }
                }
            }
        }

        if (searchActive) {
            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            searchActive = false
                            searchText = ""
                            onBackClick()
                        }
                )

                Spacer(modifier = Modifier.width(6.dp))

                SearchBar(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearchClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )
            }

            val results = remember(items, searchText) {
                items.filter {
                    it.title.contains(searchText, ignoreCase = true)
                }
            }

            LazyColumn(Modifier.fillMaxSize()) {
                items(results) { item ->
                    SearchResultRow(item) {
                        navController.navigate("detail/${Uri.encode(Gson().toJson(item))}")
                    }
                    Divider()
                }
            }

        } else {
            val gridState = rememberLazyGridState()

            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (showItemLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }
                    }
                } else {
                    items(items.size) { idx ->
                        Item(
                            item = items[idx],
                            onClick = { onItemClick(items[idx]) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingBox(height: Dp) = Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(height),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun SearchResultRow(
    item: ItemsModel,
    onClick: (ItemsModel) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item) }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.picUrl.firstOrNull()),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$${item.price}",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

