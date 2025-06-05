package com.tranthephong.fooddeliveryapp.Activity.Detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.R

@Composable
fun InfoSection(item: ItemsModel) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.title,
                fontSize = 23.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "$${item.price}",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(1f)
        )
        Image(
            painter = painterResource(R.drawable.star),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = "${item.rating}", style = MaterialTheme.typography.bodyMedium)
    }
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Select category",
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}