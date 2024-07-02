package com.dicoding.asclepius.view.history


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ItemNoteBinding

class HistoryAdapter: ListAdapter<History, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    var onClick: ((History) -> Unit)? = null

    class MyViewHolder (private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : History,onClick: ((History) -> Unit)?){

            binding.tvResult.text = item.result
            binding.tvImage.setImageURI(item.image?.toUri())
            binding.tvItemDate.text = item.date
            binding.tvDelete.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listUser = getItem(position)
        holder.bind(listUser,onClick)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}
