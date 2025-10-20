package com.example.todolist_android.adapters.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist_android.data.Task

class TaskDiffUtil(
    private val oldList: List<Task>,
    private val newList: List<Task>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return when {
            oldItem.id != newItem.id -> false
            oldItem.title != newItem.title -> false
            oldItem.done != newItem.done -> false
            else -> true
        }
    }

}