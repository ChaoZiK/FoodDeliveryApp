package com.tranthephong.fooddeliveryapp.Activity.Cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ConstraintLayout(modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()) {
            val (cartTxt) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(cartTxt) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }, text = "Your Cart",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }
        if (cartItem.value.isEmpty()) {
            Text(
                text = "Your cart is empty",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            CartList(cartItems = cartItem.value, managementCart) {
                cartItem.value = managementCart.getListCart()
                calculatorCart(managementCart, tax)
            }
            CartSummary(
                itemTotal = managementCart.getTotalFee(),
                tax = tax.value,
                delivery = 10.0,
                onCheckout = onCheckout
            )
        }
    }
}

fun calculatorCart(managementCart: ManagementCart, tax: MutableState<Double>) {
    val percentTax = 0.02
    tax.value = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
}