package com.example.reddittoppublication.postlist

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reddittoppublication.R
import com.example.reddittoppublication.databinding.FragmentPostItemBinding
import com.example.reddittoppublication.item.Data

class PostListRecyclerAdapter(
    private val onItemClicked: (String) -> Unit,
    private val onLoadNextPage: (() -> Unit)? = null
) :
    ListAdapter<Data, PostListRecyclerAdapter.MyViewHolder>(ItemDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            FragmentPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, parent.context.applicationContext)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (currentList.size - 5 == position) {
            onLoadNextPage?.invoke()
        }
        holder.bind(currentList[position], onItemClicked)
    }

    class MyViewHolder(
        private val binding: FragmentPostItemBinding,
        private val applicationContext: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Data, onItemClicked: (String) -> Unit) {
            with(binding) {
                author.text = item.author
                title.text = item.title
                numComments.text = item.numComments.toString()
                datePublication.text = item.created
                if (item.content != null) {
                    content.text = Html.fromHtml(item.content, Html.FROM_HTML_MODE_COMPACT)
                }
                //TODO() сделать вырезку ссылки из images->source->url c третьего / по ?
                Glide.with(applicationContext).load(item.thumbnail)
                    .placeholder(R.drawable.loading_screen).into(thumbnail)
                root.setOnClickListener {
                    if (item.img.endsWith(".jpg") || item.img.endsWith(".png") || item.img.endsWith(
                            ".gifv"
                        )
                    ) {
                        onItemClicked(item.img)
                    }
                }
            }
        }
    }

    class ItemDiffCallBack : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }
}