package com.oztasibrahimomer.instagramkotlin2.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oztasibrahimomer.instagramkotlin2.Post
import com.oztasibrahimomer.instagramkotlin2.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

class PostAdapter(val postList:ArrayList<Post>): RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(val binding:RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        // recycler_row ile recycler view bağlanacak!!!

        val binding =RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return PostHolder(binding)

    }

    override fun getItemCount(): Int {

        // işlemin kaç defa olacağı yapılacak

        return postList.size

    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        //recyclerview görünümde ne göreceğim

        holder.binding.emailTextRecyclerView.text=postList.get(position).email
        holder.binding.commentTextRecyclerView.text=postList.get(position).comment

        Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.imageViewRecyclerView)

    }
}