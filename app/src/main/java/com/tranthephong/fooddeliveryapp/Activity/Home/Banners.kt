package com.tranthephong.fooddeliveryapp.Activity.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tranthephong.fooddeliveryapp.Model.SliderModel
import com.tranthephong.fooddeliveryapp.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter


@Composable
fun Banners(banners: List<SliderModel>){
    AutoSlidingCarausel(banners = banners)
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun AutoSlidingCarausel(
    modifier: Modifier=Modifier.padding(top=16.dp),
    banners:List<SliderModel>
){
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    Column(modifier = modifier.fillMaxSize()){
        HorizontalPager(state = pagerState){page->
            Image(
                painter = rememberAsyncImagePainter(model = banners[page].url),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .height(150.dp)
            )
        }
        DotIndicator(
            modifier= Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            totalDots = banners.size,
            selectedIndex = if(isDragged) pagerState.currentPage else pagerState.targetPage,
            dotSize = 8.dp
        )
    }
}

@Composable
fun DotIndicator(
    modifier:Modifier=Modifier,
    totalDots:Int,
    selectedIndex:Int,
    selectedColor: Color = colorResource(R.color.darkBrown),
    unSelectedColor: Color = colorResource(R.color.grey),
    dotSize: Dp
){
    LazyRow(modifier = modifier.wrapContentSize()){
        items(totalDots){index->
            IndicatorDot(color = if(index==selectedIndex) selectedColor else unSelectedColor, size = dotSize)
            if(index!=totalDots-1){
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier= Modifier,
    size: Dp,
    color: Color
){
    Box(
        modifier = Modifier
            .size(size)
            .padding(2.dp)
            .clip(CircleShape)
            .background(color)
    )
}