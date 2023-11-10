package com.example.reddittoppublication.presentation.fragments.postlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reddittoppublication.R
import com.example.reddittoppublication.databinding.ListItemPostBinding
import com.example.reddittoppublication.domain.model.PostPage

class PostListRecyclerAdapter(
    private val onItemClicked: (List<String>) -> Unit,
    private val onLoadNextPage: (() -> Unit)? = null
) : ListAdapter<PostPage.Post, PostListRecyclerAdapter.MyViewHolder>(ItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (currentList.size - 5 == position) {
            onLoadNextPage?.invoke()
        }
        holder.bind(currentList[position], onItemClicked)
    }

    class MyViewHolder(private val binding: ListItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostPage.Post, onItemClicked: (List<String>) -> Unit) {
            val context = binding.root.context

            with(binding) {
                author.text = item.author
                title.text = item.title
                numComments.text = item.numComments.toString()
                datePublication.text = item.created
                if (item.content != null) {
                    content.text = item.content
                }
                if (item.thumbnail != "default" && item.thumbnail != "self") {
                    Glide.with(context)
                        .load(item.thumbnail)
                        .placeholder(R.drawable.loading_screen)
                        .into(thumbnail)
                } else {
                    Glide.with(context)
                        .load("")
                        .into(thumbnail)
                }
                thumbnail.setOnClickListener {
                    if (
                        item.img.first().endsWith(".jpg") ||
                        item.img.first().endsWith(".png") ||
                        item.img.first().endsWith(".gif")
                    ) {
                        onItemClicked(item.img)
                    }
                }
            }
        }
    }

    class ItemDiffCallBack : DiffUtil.ItemCallback<PostPage.Post>() {

        override fun areItemsTheSame(oldItem: PostPage.Post, newItem: PostPage.Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostPage.Post, newItem: PostPage.Post): Boolean {
            return oldItem == newItem
        }
    }
}