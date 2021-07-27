package com.joseph.unsplash_mvvm.adapters

import androidx.recyclerview.widget.DiffUtil
import com.joseph.unsplash_mvvm.models.Photo

class PhotoDiffUtil : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}