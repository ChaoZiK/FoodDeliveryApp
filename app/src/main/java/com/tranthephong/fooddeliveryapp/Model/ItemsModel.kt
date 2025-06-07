package com.tranthephong.fooddeliveryapp.Model

import java.io.Serializable

data class ItemsModel(
    var id:String="",
    var title:String="",
    var description:String="",
    var picUrl:ArrayList<String> = ArrayList(),
    var size:ArrayList<String> = ArrayList(),
    var price:Double=0.0,
    var rating:Double=0.0,
    var numberInCart:Int=0,
    var showRecommended:Boolean=false,
    var categoryId:String="",
    var category:ArrayList<String> = ArrayList()
): Serializable
