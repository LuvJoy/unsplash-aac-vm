package com.joseph.unsplash_mvvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.joseph.unsplash_mvvm.databinding.ItemPhotoBinding
import com.joseph.unsplash_mvvm.models.Photo
import javax.inject.Inject

class PhotoAdapter @Inject constructor(): ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoDiffUtil()) {
    inner class PhotoViewHolder(val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = currentList[position]
        holder.binding.apply {
            Glide.with(holder.binding.root)
                .load(photo.urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(photoImageview)
        }
    }
}