package com.example.tymextest.Test2_2

class MyList{
    companion object {
        fun findMissingNumber(list:List<Int>):List<Int> {
            val max=list.maxOrNull()?:return emptyList()
            val min=list.minOrNull()?:return emptyList()

            val fullRange = (min..max).toList()

            return fullRange.filter { it !in list }
        }
    }
}