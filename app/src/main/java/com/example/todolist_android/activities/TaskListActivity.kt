package com.example.todolist_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist_android.R
import com.example.todolist_android.adapters.TaskAdapter
import com.example.todolist_android.data.Category
import com.example.todolist_android.data.CategoryDAO
import com.example.todolist_android.data.Task
import com.example.todolist_android.data.TaskDAO
import com.example.todolist_android.databinding.ActivityTaskListBinding
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskListBinding

    lateinit var adapter: TaskAdapter
    var taskList: List<Task> = emptyList()

    lateinit var category: Category

    lateinit var taskDAO: TaskDAO
    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDAO = TaskDAO(this)
        categoryDAO = CategoryDAO(this)

        val categoryId = intent.getIntExtra("CATEGORY_ID", -1)

        category = categoryDAO.find(categoryId)!!

        supportActionBar?.title = category.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = TaskAdapter(taskList, { position ->
            // Click
            val task = taskList[position]
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            intent.putExtra("TASK_ID", task.id)
            startActivity(intent)
        }, { position ->
            // Check
            val task = taskList[position]
            task.done = !task.done
            taskDAO.update(task)
            loadData()
        }, { position ->
            // Delete
            val task = taskList[position]

            val dialog = AlertDialog.Builder(this)
                .setTitle("Borrar tarea")
                .setMessage("¿Está usted seguro de que quiere borrar la tarea: ${task.title}?")
                .setPositiveButton("Si") { dialog, which ->
                    taskDAO.delete(task)
                    loadData()
                    Snackbar.make(binding.root, "Tarea borrada correctamente", Snackbar.LENGTH_SHORT).show()
                    //Toast.makeText(this, "Tarea borrada correctamente", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .create()

            dialog.show()

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.createButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        loadData()
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

    fun loadData() {
        taskList = taskDAO.findAllByCategory(category)
        adapter.updateItems(taskList)
    }
}