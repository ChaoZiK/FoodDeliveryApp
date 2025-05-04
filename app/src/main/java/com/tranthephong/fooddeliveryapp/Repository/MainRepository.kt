package com.tranthephong.fooddeliveryapp.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tranthephong.fooddeliveryapp.Model.SliderModel

class MainRepository {

    private val firebaseDatabase = FirebaseDatabase.getInstance("https://fooddeliveryapp-4cc23-default-rtdb.asia-southeast1.firebasedatabase.app")

    fun loadBanner():LiveData<MutableList<SliderModel>>{
        val listData=MutableLiveData<MutableList<SliderModel>>()
        val ref=firebaseDatabase.getReference("Banner")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<SliderModel>()
                for (data in snapshot.children){
//                    val item=data.getValue(SliderModel::class.java)
//                    item?.let {lists.add(it)}
                    val url = data.child("url").getValue(String::class.java)
                    if (url != null) {
                        lists.add(SliderModel(url))
                    }
                }
                Log.d("FIREBASE_DEBUG", "Banner list size: ${lists.size}")
                listData.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read banners: ${error.message}")
            }
        })
        return listData
    }
}