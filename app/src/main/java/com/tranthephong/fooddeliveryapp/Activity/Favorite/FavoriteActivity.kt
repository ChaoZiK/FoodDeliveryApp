package com.tranthephong.fooddeliveryapp.Activity.Favorite

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tranthephong.fooddeliveryapp.Model.ItemsModel

@Composable
fun FavoriteScreen(
    onItemClick: (ItemsModel) -> Unit
) {
    val context = LocalContext.current
    var favoriteItems by remember { mutableStateOf<List<ItemsModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val user = FirebaseAuth.getInstance().currentUser
    val database =
        FirebaseDatabase.getInstance("https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app")

    LaunchedEffect(user) {
        if (user == null) {
            Toast.makeText(context, "Please log in to see your favorite items", Toast.LENGTH_SHORT)
                .show()
            isLoading = false
            return@LaunchedEffect
        }

        val ref = database.getReference("Users/${user.uid}/Favorites")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<ItemsModel>()
                for (child in snapshot.children) {
                    val item = child.getValue(ItemsModel::class.java)
                    item?.let { items.add(it) }
                }
                favoriteItems = items
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load favorites", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)
        ) {
            val (titleTxt) = createRefs()

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(titleTxt) { centerTo(parent) },
                text = "Favorites",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            favoriteItems.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No favorite items yet", fontSize = 18.sp, color = Color.Gray)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    items(favoriteItems) { item ->
                        FavoriteItemRow(
                            item = item,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}
