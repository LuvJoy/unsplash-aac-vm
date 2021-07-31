package com.joseph.unsplash_mvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.joseph.unsplash_mvvm.databinding.ItemUserPhotoBinding
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.util.convertToSimpleDateFormat

class UsersPhotoAdapter :
    ListAdapter<Photo, UsersPhotoAdapter.UsersPhotoViewHolder>(PhotoDiffUtil()) {
    inner class UsersPhotoViewHolder(val binding: ItemUserPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var itemClickListener: ((Photo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersPhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserPhotoBinding.inflate(layoutInflater, parent, false)
        return UsersPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersPhotoViewHolder, position: Int) {
        val photo = currentList[position]
        with(holder.binding) {
            if(photo.description == "") {
                titleTextview.visibility = View.GONE
            } else {
                titleTextview.text = photo.description
            }

            Glide.with(holder.binding.root)
                .load(photo.urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(photoImageview)

            root.setOnClickListener {
                itemClickListener?.invoke(photo)
            }
        }
    }

    fun setItemClickListener(listener: (Photo) -> Unit) {
        itemClickListener = listener
    }
}