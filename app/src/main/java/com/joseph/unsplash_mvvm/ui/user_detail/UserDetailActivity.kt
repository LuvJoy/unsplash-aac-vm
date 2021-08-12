package com.joseph.unsplash_mvvm.ui.user_detail

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.joseph.unsplash_mvvm.adapters.UsersPhotoAdapter
import com.joseph.unsplash_mvvm.databinding.ActivityUserDetailBinding
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.ui.detail.DetailActivity
import com.joseph.unsplash_mvvm.ui.user_detail.UserDetailViewModel.UserDetailEvent
import com.joseph.unsplash_mvvm.util.setInfinityScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel by viewModels<UserDetailViewModel>()
    private val usersPhotosAdapter by lazy { UsersPhotoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.BLACK

        initRecyclerView()
        collectUserProfile()
        collectUsersPhotos()
    }

    private fun initRecyclerView() = with(binding) {
        val layoutManager = LinearLayoutManager(this@UserDetailActivity, LinearLayoutManager.VERTICAL, false)
        userPhotoRecyclerview.layoutManager = layoutManager
        userPhotoRecyclerview.adapter = usersPhotosAdapter
        usersPhotosAdapter.setItemClickListener { photo ->
            val intent = Intent(this@UserDetailActivity, DetailActivity::class.java)
            intent.putExtra("photo", photo)
            startActivity(intent)
        }
        userPhotoRecyclerview.setInfinityScrollListener { viewModel.getUsersPhotos() }
    }

    private fun collectUserProfile() = lifecycleScope.launchWhenStarted {
        viewModel.user.collect { user ->
            user ?: return@collect
            settingUserProfile(user)
        }
    }

    private fun collectUsersPhotos() = lifecycleScope.launchWhenStarted {
        viewModel.usersPhotos.collect { event ->
            when(event) {
                is UserDetailEvent.Loading -> {
                    binding.progressLottie.isVisible = true
                }
                is UserDetailEvent.LoadUsersPhotoSuccess -> {
                    binding.progressLottie.isVisible = false
                    if(event.data.isNotEmpty()) {
                        settingRecyclerView(event.data)
                    } else {
                        Toast.makeText(this@UserDetailActivity, "Users photos are empty", Toast.LENGTH_SHORT).show()
                        binding.userPhotoRecyclerview.visibility = View.INVISIBLE
                    }
                }
                is UserDetailEvent.LoadUsersPhotoFailed -> {
                    binding.progressLottie.isVisible = false
                    Toast.makeText(this@UserDetailActivity, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun settingUserProfile(user: User) = with(binding) {
        userNameTextview.text = user.name
        userUsernameTextview.text = user.username
        Glide.with(this@UserDetailActivity)
            .load(user.profileImage?.large)
            .apply(
                RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL)
            ).into(userProfileImageview)
    }

    private fun settingRecyclerView(photos: List<Photo>) = with(binding) {
        Timber.d("TAG + ${photos.toString()}")
        usersPhotosAdapter.submitList(usersPhotosAdapter.currentList + photos)
    }
}