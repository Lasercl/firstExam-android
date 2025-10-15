package com.example.firstexam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstexam.databinding.ItemRowHeroBinding
import com.example.firstexam.response.ListEventsItem

class ListEventAdapter : ListAdapter<ListEventsItem, ListEventAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickListener: ((ListEventsItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ListEventsItem) -> Unit) {
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val eventItem = getItem(position)
        holder.bind(eventItem)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(eventItem)
        }
    }
    class MyViewHolder(val binding: ItemRowHeroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem){
//            binding.tvItem.text = "${review.review}\n- ${review.name}"
            binding.tvItemName.text="${event.name}"
            binding.tvItemDescription.text="${event.summary}"
            Glide.with(binding.root.context)
                .load("${event.imageLogo}")
                .into(binding.imgItemPhoto)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}