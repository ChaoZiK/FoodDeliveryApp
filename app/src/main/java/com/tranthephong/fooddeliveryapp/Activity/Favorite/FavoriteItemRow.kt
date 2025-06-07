package com.tranthephong.fooddeliveryapp.Activity.Favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tranthephong.fooddeliveryapp.Model.ItemsModel

@Composable
fun FavoriteItemRow(item: ItemsModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.picUrl.firstOrNull()),
            contentDescription = item.title,
            modifier = Modifier
                .size(80.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = item.description, fontSize = 14.sp, maxLines = 2)
        }
    }
}
