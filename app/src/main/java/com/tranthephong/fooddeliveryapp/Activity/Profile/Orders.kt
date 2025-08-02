package com.tranthephong.fooddeliveryapp.Activity.Profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.tranthephong.fooddeliveryapp.Model.Order
import com.tranthephong.fooddeliveryapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Orders(
    onBack: () -> Unit,
    onOrderClick: (orderJson: String) -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseDatabase.getInstance(
        "https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app/"
    )

    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    val local = LocalContext.current

    LaunchedEffect(user) {
        if (user == null) {
            Toast.makeText(local, "Please log in", Toast.LENGTH_SHORT).show()
            loading = false
            return@LaunchedEffect
        }
        val ref = db.getReference("Users/${user.uid}/Orders")
        ref.get().addOnSuccessListener { snap ->
            val list = mutableListOf<Order>()
            snap.children.forEach { cs ->
                cs.getValue(Order::class.java)?.let { list.add(it) }
            }
            orders = list
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        BackArrow(onBack)

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading…")
            }
        } else if (orders.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No past orders", fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(orders) { idx, order ->
                    val date = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
                        .format(Date(order.timestamp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                onOrderClick(Gson().toJson(order))
                            },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Order ${idx + 1}",
                                    modifier = Modifier.weight(1f),
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = date,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Items: ${order.items.size}", fontSize = 14.sp)
                            Text(
                                "Total price: \$${"%.2f".format(order.totalPrice)}",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackArrow(onBack: () -> Unit) {
    Icon(
        painter = painterResource(R.drawable.back_arrow),
        contentDescription = "Back",
        modifier = Modifier
            .padding(top = 32.dp, bottom = 16.dp)
            .clickable(onClick = onBack)
    )
}
