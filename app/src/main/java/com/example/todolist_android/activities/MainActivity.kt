package com.example.todolist_android.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist_android.R
import com.example.todolist_android.data.Category
import com.example.todolist_android.data.CategoryDAO

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val categoryDAO = CategoryDAO(this)

        val category1 = Category(-1, "Compra")
        val category2 = Category(-1, "Recordatorios")

        categoryDAO.insert(category1)
        categoryDAO.insert(category2)

        val categories = categoryDAO.findAll()

        Log.i("DATABASE", categories.toString())
    }
}