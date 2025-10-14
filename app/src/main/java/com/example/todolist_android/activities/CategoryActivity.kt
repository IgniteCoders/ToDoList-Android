package com.example.todolist_android.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist_android.R
import com.example.todolist_android.data.Category
import com.example.todolist_android.data.CategoryDAO
import com.example.todolist_android.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding

    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.editText?.text.toString()

            val category = Category(-1, name)

            categoryDAO.insert(category)

            finish()
        }
    }
}