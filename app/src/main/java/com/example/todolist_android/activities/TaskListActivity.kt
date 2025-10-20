package com.example.todolist_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist_android.R
import com.example.todolist_android.adapters.HeaderAdapter
import com.example.todolist_android.adapters.TaskAdapter
import com.example.todolist_android.adapters.TaskViewHolder
import com.example.todolist_android.data.Category
import com.example.todolist_android.data.CategoryDAO
import com.example.todolist_android.data.Task
import com.example.todolist_android.data.TaskDAO
import com.example.todolist_android.databinding.ActivityTaskListBinding
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskListBinding

    lateinit var taskDoneAdapter: TaskAdapter
    lateinit var taskToDoAdapter: TaskAdapter
    lateinit var doneHeaderAdapter: HeaderAdapter
    lateinit var toDoHeaderAdapter: HeaderAdapter
    var taskDoneList: List<Task> = emptyList()
    var taskToDoList: List<Task> = emptyList()

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

        taskDoneAdapter = TaskAdapter(taskDoneList, { position ->
            // Click
            val task = taskDoneList[position]
            onItemClick(task)
        }, { position ->
            // Check
            val task = taskDoneList[position]
            onItemCheck(task)
        }, { position ->
            // Delete
            val task = taskDoneList[position]
            onItemDelete(task)
        })

        taskToDoAdapter = TaskAdapter(taskToDoList, { position ->
            // Click
            val task = taskToDoList[position]
            onItemClick(task)
        }, { position ->
            // Check
            val task = taskToDoList[position]
            onItemCheck(task)
        }, { position ->
            // Delete
            val task = taskToDoList[position]
            onItemDelete(task)
        })

        toDoHeaderAdapter = HeaderAdapter("Tareas pendientes")
        doneHeaderAdapter = HeaderAdapter("Tareas completadas")

        binding.recyclerView.adapter = ConcatAdapter(toDoHeaderAdapter, taskToDoAdapter, doneHeaderAdapter, taskDoneAdapter)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.createButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            startActivity(intent)
        }

        configureGestures()
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

    fun onItemClick(task: Task) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra("CATEGORY_ID", category.id)
        intent.putExtra("TASK_ID", task.id)
        startActivity(intent)
    }

    fun onItemCheck(task: Task) {
        task.done = !task.done
        taskDAO.update(task)
        loadData()
    }

    fun onItemDelete(task: Task) {
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
    }

    fun loadData() {
        taskDoneList = taskDAO.findAllByCategoryAndDone(category, true)
        taskToDoList = taskDAO.findAllByCategoryAndDone(category, false)
        taskDoneAdapter.updateItems(taskDoneList)
        taskToDoAdapter.updateItems(taskToDoList)
    }

    private fun configureGestures() {
        val gestures = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    //recyclerView.adapter?.notifyItemMoved(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
                    return false
                }

                // No se reordenaran las tareas por ahora
                override fun isLongPressDragEnabled(): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = (viewHolder as TaskViewHolder).task

                    if (direction == ItemTouchHelper.LEFT) {
                        onItemDelete(task)
                        binding.recyclerView.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                    } else {
                        onItemCheck(task)
                    }
                }
            })
        gestures.attachToRecyclerView(binding.recyclerView)
    }
}