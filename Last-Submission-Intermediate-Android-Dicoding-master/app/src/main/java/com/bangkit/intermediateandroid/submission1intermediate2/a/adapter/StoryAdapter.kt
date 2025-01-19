package com.bangkit.intermediateandroid.submission1intermediate2.a.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bangkit.intermediateandroid.submission1intermediate2.a.activity.DetailActivity
import com.bangkit.intermediateandroid.submission1intermediate2.databinding.ItemStoryBinding

import com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.response.ListStoryItem
import com.bangkit.intermediateandroid.submission1intermediate2.a.dateConverter

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataStory = getItem(position)
        if (dataStory != null) {
            holder.bind(dataStory)
        }
    }
    class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data : ListStoryItem) {
            binding.tvItemName.text = data.name
            binding.tvItemDeskripsi.text = data.description
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvItemTanggal.text = dateConverter(data.createdAt)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("id", data.id)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "images"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvItemDeskripsi, "desc"),
                        Pair(binding.tvItemTanggal, "date")
                    )
                itemView.context.startActivity(intent,optionsCompat.toBundle())
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}