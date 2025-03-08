package com.salmonboy.core.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salmonboy.core.databinding.ItemDicodingEventBinding
import com.salmonboy.core.domain.model.Event

class EventAdapter : ListAdapter<Event, EventAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Event) -> Unit)? = null

   inner class ViewHolder(private val binding: ItemDicodingEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.tvDicodingEventName.text = event.name
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(binding.ivDicodingEvent)
        }

       init {
           itemView.setOnClickListener{
               onItemClick?.invoke(getItem(bindingAdapterPosition))
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemDicodingEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Event> =
            object : DiffUtil.ItemCallback<Event>() {
                override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                    return oldItem.id == newItem.id
                }
                override fun areContentsTheSame(
                    oldItem: Event,
                    newItem: Event,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}