package com.example.tymextest.Test1.Ui.View.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tymextest.MainActivity
import com.example.tymextest.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plash_screen)
        GlobalScope.launch {
            delay(1000) // Độ trễ 3 giây
            startActivity(Intent(this@PlashScreenActivity, MainActivity::class.java))
            finish()
        }
    }
}