package com.example.todolist_android.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist_android.R
import com.example.todolist_android.data.Category
import com.example.todolist_android.data.CategoryDAO
import com.example.todolist_android.data.Task
import com.example.todolist_android.data.TaskDAO
import com.example.todolist_android.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskBinding


    lateinit var taskDAO: TaskDAO
    lateinit var categoryDAO: CategoryDAO

    lateinit var task: Task
    lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        val id = intent.getIntExtra("TASK_ID", -1)

        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

        category = categoryDAO.find(categoryId)!!

        if (id != -1) {
            // Edit
            task = taskDAO.find(id)!!
            supportActionBar?.title = "Editar tarea"
        } else {
            // Crear
            task = Task(-1, "", false, category)
            supportActionBar?.title = "Crear tarea"
        }

        binding.titleEditText.editText?.setText(task.title)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.editText?.text.toString()

            task.title = title

            if (task.id == -1) {
                taskDAO.insert(task)
            } else {
                taskDAO.update(task)
            }

            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}