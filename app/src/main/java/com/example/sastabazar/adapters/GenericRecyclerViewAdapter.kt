package com.example.sastabazar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericRecyclerViewAdapter<T, VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val itemBinder: ItemBinder<T, VB>,
    private val itemDataList: List<T>
) : RecyclerView.Adapter<GenericRecyclerViewAdapter.ItemViewHolder<T, VB>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<T, VB> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = bindingInflater(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder<T, VB>, position: Int) {
        val item = itemDataList[position]
        itemBinder.bindItem(holder.binding, item)
    }

    override fun getItemCount(): Int {
        return itemDataList.size
    }

    interface ItemBinder<T, VB : ViewBinding> {
        fun bindItem(binding: VB, item: T)
    }

    class ItemViewHolder<T, VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
