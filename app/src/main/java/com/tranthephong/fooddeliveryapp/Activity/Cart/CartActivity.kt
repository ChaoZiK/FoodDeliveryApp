package com.tranthephong.fooddeliveryapp.Activity.Cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranthephong.fooddeliveryapp.Helper.ManagementCart

@Composable
fun CartScreen(
    onItemChange: () -> Unit,
    onCheckout: () -> Unit
) {
    val context = LocalContext.current
    val managementCart = remember { ManagementCart(context) }
    var cartItem = remember { mutableStateOf(managementCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }

    LaunchedEffect(cartItem.value) {
        calculatorCart(managementCart, tax)
    }

    calculatorCart(managementCart, tax)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                "Your Cart",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (cartItem.value.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your cart is empty",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(items = cartItem.value, key = { it.id }) { item ->
                CartItem(
                    cartItems = cartItem.value,
                    item = item,
                    managementCart = managementCart
                ) {
                    cartItem.value = managementCart.getListCart()
                    calculatorCart(managementCart, tax)
                    onItemChange()
                }
            }

            item {
                CartSummary(
                    itemTotal = managementCart.getTotalFee(),
                    tax = tax.value,
                    delivery = 10.0,
                    onCheckout = onCheckout
                )
            }
        }
    }
}

fun calculatorCart(managementCart: ManagementCart, tax: MutableState<Double>) {
    val percentTax = 0.02
    tax.value = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
}