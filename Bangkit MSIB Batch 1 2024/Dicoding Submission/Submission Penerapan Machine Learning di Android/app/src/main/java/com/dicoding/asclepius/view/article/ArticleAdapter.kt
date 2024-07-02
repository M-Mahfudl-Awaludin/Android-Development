package com.dicoding.asclepius.view.article


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ItemReviewBinding
import com.dicoding.asclepius.response.Article


class ArticleAdapter(
    private val listItem: List<Article>,
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.NewsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(listItem[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(listItem[position])
        }
    }

    inner class NewsViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article) {
            binding.apply {
                ivNews.load(item.urlToImage) {
                    crossfade(true)
                    placeholder(R.drawable.ic_place_holder)
                    crossfade(100)
                }
                tvTitle.text = item.title
                tvSource.text = item.source.name
                tvAuthor.text = item.publishedAt
            }
        }
    }
}