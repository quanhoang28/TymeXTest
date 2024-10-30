package com.example.tymextest.Test2_1

import android.util.Log
import java.time.LocalDateTime

class Product(
    private var name: String,
    private var price: Double,
    private var quantity: Int
){
    fun printInFor(){
        Log.v("testInput","name: ${name} , price ${price}, quantity: ${quantity}")
    }

    fun getName():String{
        return name
    }

    fun getPrice():Double{
        return price
    }

    fun getQuantity():Int{
        return quantity
    }

    fun getTotalValue():Double{
        return price*quantity;
    }
}