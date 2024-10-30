package com.example.tymextest.Test1.Data.Model

data class LastestRateResponse (
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)