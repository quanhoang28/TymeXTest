package com.example.tymextest.Test2_1

import android.util.Log
import androidx.constraintlayout.motion.widget.Debug

class Inventory(private var products:List<Product>) {
    fun printInventory() {
        products.forEach { it.printInFor() }
    }

    fun getTotalValue():Double{
        var total:Double= products.sumOf {
           it.getTotalValue()
        }
        Log.v("testInput",total.toString())
        return total
    }

    fun getHighestPriceProduct():Product?{
        var product:Product?=products.maxByOrNull { it.getPrice() }
        Log.v("testInput",product?.getName()?:"")
        return product
    }

    fun checkStock(productName: String): Boolean {
        var inStock=products.any { it.getName().equals(productName, ignoreCase = true) }
        Log.v("testInput",inStock.toString())
        return inStock
    }

    fun sortByPrice(ascending:Boolean){
        products=if(ascending){
            products.sortedBy { it.getPrice() }
        }else{
            products.sortedByDescending { it.getPrice() }
        }
        printInventory()
    }

    fun sortByQuantity(ascending:Boolean){
        products= if(ascending){
            products.sortedBy { it.getQuantity() }
        }else{
            products.sortedByDescending { it.getQuantity() }
        }
        printInventory()
    }
}