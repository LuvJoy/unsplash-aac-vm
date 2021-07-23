package com.joseph.unsplash_mvvm.ui.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.joseph.unsplash_mvvm.R
import com.joseph.unsplash_mvvm.databinding.FragmentHomeBinding
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.ui.main.HomeViewModel
import com.joseph.unsplash_mvvm.ui.main.HomeViewModel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectRandomPhoto()
        collectRandomPhotoUser()
        collectErrorEvent()
    }

    private fun collectErrorEvent() {
        lifecycleScope.launchWhenStarted {
            viewModel.errorEvent.collect { event ->
                if(event is Event.ErrorEvent) Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun collectRandomPhoto() {
        lifecycleScope.launchWhenStarted {
            viewModel.randomPhoto.collect { photo ->
                photo?.let { setRandomImage(it) }
            }
        }
    }

    private fun collectRandomPhotoUser() {
        lifecycleScope.launchWhenStarted {
            viewModel.userProfile.collect { user ->
                user?.let { setRandomPhotoUserProfile(it) }
            }
        }
    }

    private fun setRandomPhotoUserProfile(user: User) {
        val profileImage = user.profileImage?.large
        val name = user.name
        val userName = user.username

        with(binding) {
            randomUsernameTextview.text = "@$userName"
            randomNameTextview.text = name
            Glide.with(requireContext())
                .load(profileImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(randomUserProfileImageview)
        }
    }

    private fun setRandomImage(photo: Photo) {
        val photoUrl = photo.urls?.full
        Glide.with(requireContext())
            .load(photoUrl)
            .thumbnail(0.05f)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.maintopicImgview)
    }
}