package com.joseph.unsplash_mvvm.util

import android.content.Context
import android.view.View
import android.view.animation.Animation

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