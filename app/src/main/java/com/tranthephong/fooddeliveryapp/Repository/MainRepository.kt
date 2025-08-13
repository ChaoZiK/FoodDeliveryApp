package com.tranthephong.fooddeliveryapp.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.Model.SliderModel

class MainRepository {

    private val firebaseDatabase =
        FirebaseDatabase.getInstance("https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app")

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        val listData = MutableLiveData<MutableList<SliderModel>>()
        val ref = firebaseDatabase.getReference("Banner")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (data in snapshot.children) {
                    val item = data.getValue(SliderModel::class.java)
                    item?.let { lists.add(it) }
                }
                listData.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read banners: ${error.message}")
            }
        })
        return listData
    }

//    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
//        val listData = MutableLiveData<MutableList<CategoryModel>>()
//        val ref = firebaseDatabase.getReference("Category")
//
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val lists = mutableListOf<CategoryModel>()
//                for (data in snapshot.children) {
//                    val item = data.getValue(CategoryModel::class.java)
//                    item?.let { lists.add(it) }
//                }
//                listData.value = lists
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("Firebase", "Failed to read category: ${error.message}")
//            }
//        })
//        return listData
//    }

    fun loadItem(): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (data in snapshot.children) {
                    val item = data.getValue(ItemsModel::class.java)
                    if (item != null) {
                        item.id = data.key ?: ""
                        lists.add(item)
                    }
                }
                listData.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read item: ${error.message}")
            }
        })
        return listData
    }


    fun loadFiltered(id: String): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")
        val query: Query = ref.orderByChild("categoryId").equalTo(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                listData.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return listData
    }
}