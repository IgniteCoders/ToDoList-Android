package com.example.todolist_android.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist_android.R
import com.example.todolist_android.adapters.CategoryAdapter
import com.example.todolist_android.data.Category
import com.example.todolist_android.data.CategoryDAO
import com.example.todolist_android.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var adapter: CategoryAdapter
    var categoryList: List<Category> = emptyList()

    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Mis categorias"

        categoryDAO = CategoryDAO(this)

        adapter = CategoryAdapter(categoryList, { position ->
            // Click
            val category = categoryList[position]
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            startActivity(intent)
        }, { position ->
            // Edit
            val category = categoryList[position]
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            startActivity(intent)
        }, { position ->
            // Delete
            val category = categoryList[position]

            val dialog = AlertDialog.Builder(this)
                .setTitle("Borrar categoria")
                .setMessage("¿Está usted seguro de que quiere borrar la categoría: ${category.name}?")
                .setPositiveButton("Si") { dialog, which ->
                    categoryDAO.delete(category.id)
                    loadData()
                    Snackbar.make(binding.root, "Categoría borrada correctamente", Snackbar.LENGTH_SHORT).show()
                    //Toast.makeText(this, "Categoría borrada correctamente", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .create()

            dialog.show()

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        binding.createButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    fun loadData() {
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)
    }
}