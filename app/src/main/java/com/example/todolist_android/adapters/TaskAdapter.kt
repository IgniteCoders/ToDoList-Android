package com.example.todolist_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist_android.adapters.utils.TaskDiffUtil
import com.example.todolist_android.data.Task
import com.example.todolist_android.databinding.ItemTaskBinding

class TaskAdapter(
    var items: List<Task>,
    val onClickListener: (Int) -> Unit,
    val onCheckListener: (Int) -> Unit,
    val onDeleteListener: (Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    // Cual es la vista para los elementos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return TaskViewHolder(binding)
    }

    // Cuales son los datos para el elemento
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = items[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onClickListener(holder.bindingAdapterPosition)
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { _, _ ->
            if(holder.binding.doneCheckBox.isPressed) {
                onCheckListener(holder.bindingAdapterPosition)
            }
        }
        holder.binding.deleteButton.setOnClickListener {
            onDeleteListener(holder.bindingAdapterPosition)
        }
    }

    // Cuantos elementos se quieren listar?
    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Task>) {
        val diffUtil = TaskDiffUtil(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.items = items
        diffResult.dispatchUpdatesTo(this)
    }
}

class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.titleTextView.text = task.title
        binding.doneCheckBox.isChecked = task.done
        //binding.doneCheckBox.isEnabled = !task.done
    }
}