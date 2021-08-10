package com.joseph.unsplash_mvvm.util

import android.content.Context
import android.view.View
import android.view.animation.Animation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

fun View.hide(context: Context, animation: Animation? = null) {
    if (animation == null) {
        this.visibility = View.INVISIBLE
    } else {
        this.startAnimation(animation)
    }
}

fun View.show(context: Context, animation: Animation? = null) {
    if (animation == null) {
        this.visibility = View.VISIBLE
    } else {
        this.startAnimation(animation)
    }
}

fun RecyclerView.setupInfinityScrollListener(load: () -> Unit) {
    this.addOnScrollListener (object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val total = recyclerView.adapter?.itemCount
            val pos = layoutManager.findLastCompletelyVisibleItemPosition()

            if(pos == total?.minus(1) ?: 0) {
                load()
            }
        }
    })
}
