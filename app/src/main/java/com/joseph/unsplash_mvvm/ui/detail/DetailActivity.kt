package com.joseph.unsplash_mvvm.ui.detail

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.animation.AnimationUtils
import com.joseph.unsplash_mvvm.R
import com.joseph.unsplash_mvvm.databinding.ActivityDetailBinding
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.util.hide
import com.joseph.unsplash_mvvm.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private val fadeOutAnimation by lazy { loadAnimation(this, R.anim.fade_out) }
    private val fadeInAnimation by lazy { loadAnimation(this, R.anim.fade_in) }

    private var showProfile = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        collectUserData()
        collectPhotoData()
        collectionPhotoInfoVisible()

        binding.root.setOnClickListener {
            viewModel.onTouchScreen()
        }
    }

    private fun collectionPhotoInfoVisible() = lifecycleScope.launchWhenStarted {
        viewModel.showPhotoInfoVisible.collect { isShown ->
            if(isShown) {
                showPhotoData()
            } else {
                hidePhotoData()
            }
        }
    }

    private fun showPhotoData() = with(binding) {
        userProfileLayout.root.show(this@DetailActivity, fadeInAnimation)
        bottomShadow.show(this@DetailActivity, fadeInAnimation)
        topShadow.show(this@DetailActivity, fadeInAnimation)
        photoLocationTextview.show(this@DetailActivity, fadeInAnimation)
        photoDescriptionTextview.show(this@DetailActivity, fadeInAnimation)
    }

    private fun hidePhotoData() = with(binding) {
        userProfileLayout.root.hide(this@DetailActivity, fadeOutAnimation)
        bottomShadow.hide(this@DetailActivity, fadeOutAnimation)
        topShadow.hide(this@DetailActivity, fadeOutAnimation)
        photoLocationTextview.hide(this@DetailActivity, fadeOutAnimation)
        photoDescriptionTextview.hide(this@DetailActivity, fadeOutAnimation)
    }

    private fun collectUserData() = lifecycleScope.launchWhenStarted {
        viewModel.userProfile.collect { event ->
            when (event) {
                is DetailViewModel.Event.LoadUserProfileEvent -> {
                    settingUserProfileViews(event.data)
                }
                is DetailViewModel.Event.LoadUserProfileErrorEvent -> {
                    Toast.makeText(
                        this@DetailActivity,
                        "LoadUserData Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun collectPhotoData() = lifecycleScope.launchWhenStarted {
        viewModel.photo.collect { photo ->
            photo?.let {
                settingPhotoViews(it)
            }
        }
    }

    private fun settingUserProfileViews(userProfile: User) = with(binding) {
        userProfileLayout.usernameTextview.text = "@" + userProfile.username
        userProfileLayout.nameTextview.text = userProfile.name
        Glide.with(this@DetailActivity)
            .load(userProfile.profileImage?.medium)
            .apply(
                RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL)
            ).into(binding.userProfileLayout.userprofileImageview)
    }

    private fun settingPhotoViews(photo: Photo) = with(binding) {
        photoDescriptionTextview.text = photo.description ?: ""
        photoLocationTextview.text =
            "${photo.location?.city ?: ""} ${photo.location?.country ?: ""}"
        Glide.with(this@DetailActivity)
            .load(photo.urls?.full)
            .apply(
                RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL)
            ).into(binding.photoImageview)
    }

    override fun onBackPressed() {
        hidePhotoData()
        supportFinishAfterTransition()
    }
}