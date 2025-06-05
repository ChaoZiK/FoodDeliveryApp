package com.tranthephong.fooddeliveryapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tranthephong.fooddeliveryapp.Model.CategoryModel
import com.tranthephong.fooddeliveryapp.Model.ItemsModel
import com.tranthephong.fooddeliveryapp.Model.SliderModel
import com.tranthephong.fooddeliveryapp.Repository.MainRepository

class MainViewModel(): ViewModel() {
    private val repository = MainRepository()

    fun loadBanner():LiveData<MutableList<SliderModel>>{
        return repository.loadBanner()
    }

    fun loadCategory():LiveData<MutableList<CategoryModel>>{
        return repository.loadCategory()
    }

    fun loadItem():LiveData<MutableList<ItemsModel>>{
        return repository.loadItem()
    }

    fun loadFiltered(id:String):LiveData<MutableList<ItemsModel>>{
        return repository.loadFiltered(id)
    }
}