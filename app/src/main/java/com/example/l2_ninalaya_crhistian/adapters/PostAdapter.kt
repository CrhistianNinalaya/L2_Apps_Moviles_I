package com.example.l2_ninalaya_crhistian.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.l2_ninalaya_crhistian.R
import com.example.l2_ninalaya_crhistian.databinding.ItemPostBinding
import com.example.l2_ninalaya_crhistian.model.Posts

class PostAdapter(private var postsList: List<Posts>): RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = postsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }
    fun setearPost(nuevoPost: List<Posts>){
        postsList = nuevoPost
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding:ItemPostBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(post: Posts){
            binding.tvId.text = post.id.toString()
            binding.tvUserId.text = post.userId.toString()
            binding.tvTitle.text = post.title
            binding.tvBody.text = post.body
        }
    }

}
