package com.example.todolist_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist_android.data.Task
import com.example.todolist_android.databinding.ItemHeaderBinding
import com.example.todolist_android.databinding.ItemTaskBinding

class HeaderAdapter(var title: String) : RecyclerView.Adapter<HeaderViewHolder>() {

    // Cual es la vista para los elementos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHeaderBinding.inflate(layoutInflater, parent, false)
        return HeaderViewHolder(binding)
    }

    // Cuales son los datos para el elemento
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.render(title)
    }

    // Cuantos elementos se quieren listar?
    override fun getItemCount(): Int {
        return 1
    }
}

class HeaderViewHolder(val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(title: String) {
        binding.titleTextView.text = title
    }
}