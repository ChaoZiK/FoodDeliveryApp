package com.tranthephong.fooddeliveryapp.Activity.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tranthephong.fooddeliveryapp.Model.Order

@Composable
fun OrderDetail(
    order: Order,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        BackArrow(onBack)

        Spacer(Modifier.height(8.dp))

        Text(
            "Order Details",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        Divider()

        order.items.forEach { item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = item.picUrl.firstOrNull()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        item.title,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("\$${"%.2f".format(item.price)}", fontSize = 14.sp)
                    Text("Quantity: ${item.numberInCart}", fontSize = 12.sp)
                }
                Text(
                    "\$${"%.2f".format(item.price * item.numberInCart)}",
                    fontSize = 16.sp
                )
            }
            Divider()
        }
    }
}