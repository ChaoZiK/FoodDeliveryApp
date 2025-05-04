package com.tranthephong.fooddeliveryapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tranthephong.fooddeliveryapp.Model.SliderModel
import com.tranthephong.fooddeliveryapp.Repository.MainRepository

class MainViewModel(): ViewModel() {
    private val repository = MainRepository()

    fun loadBanner():LiveData<MutableList<SliderModel>>{
        return repository.loadBanner()
    }
}