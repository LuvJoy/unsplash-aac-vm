package com.joseph.unsplash_mvvm.ui.main.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.joseph.unsplash_mvvm.R
import com.joseph.unsplash_mvvm.adapters.PhotoAdapter
import com.joseph.unsplash_mvvm.databinding.FragmentHomeBinding
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.ui.detail.DetailActivity
import com.joseph.unsplash_mvvm.ui.main.HomeViewModel
import com.joseph.unsplash_mvvm.ui.main.HomeViewModel.*
import com.joseph.unsplash_mvvm.util.Resource
import com.joseph.unsplash_mvvm.util.setInfinityScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val natureAdapter: PhotoAdapter by lazy { PhotoAdapter() }
    private val animalAdapter: PhotoAdapter by lazy { PhotoAdapter() }

    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val navController by lazy { findNavController() }
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        collectRandomPhoto()
        collectRandomPhotoUser()
        collectNaturePhotos()
        collectAnimalPhotos()
    }

    private fun initRecyclerView() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.natureRecyclerview.adapter = natureAdapter
        binding.natureRecyclerview.layoutManager = layoutManager
        natureAdapter.setItemClickListener { photo, view ->
            navigateToDetailActivity(photo)
        }
        binding.natureRecyclerview.setInfinityScrollListener { viewModel.getNaturePhotos() }

        val layoutManager2 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.animalRecyclerview.adapter = animalAdapter
        binding.animalRecyclerview.layoutManager = layoutManager2
        animalAdapter.setItemClickListener { photo, view ->
            navigateToDetailActivity(photo)
        }
        binding.animalRecyclerview.setInfinityScrollListener { viewModel.getAnimalPhotos() }
    }

    private fun navigateToDetailActivity(photo: Photo) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("photo", photo)
        }
        startActivity(intent)
    }

    private fun collectRandomPhoto() = lifecycleScope.launchWhenStarted {
        viewModel.randomPhoto.collect { state ->
            state ?: return@collect
            when (state) {
                is Resource.Success -> {
                    if(state.data != null) {
                        binding.randomImageImageview.setOnClickListener { navigateToDetailActivity(state.data) }
                        setRandomImage(state.data)
                    } else {
                        Toast.makeText(requireContext(), "Random Data is Empty", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.progressLottie.isVisible = true
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun collectRandomPhotoUser() = lifecycleScope.launchWhenStarted {
        viewModel.userProfile.collect { state ->
            state ?: return@collect
            when (state) {
                is Resource.Success -> {
                    if(state.data != null) {
                        setRandomPhotoUserProfile(state.data)
                    } else {
                        Toast.makeText(requireContext(), "Can't Found User Data", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun collectNaturePhotos() = lifecycleScope.launchWhenStarted {
        viewModel.naturePhotos.collect { state ->
            state ?: return@collect
            when(state) {
                is Resource.Success -> {
                    if(state.data?.results != null) {
                        natureAdapter.submitList(natureAdapter.currentList + state.data.results)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun collectAnimalPhotos() = lifecycleScope.launchWhenStarted {
        viewModel.animalPhotos.collect { state ->
            state ?: return@collect
            when(state) {
                is Resource.Success -> {
                    if(state.data?.results != null) {
                        animalAdapter.submitList(animalAdapter.currentList + state.data.results)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
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
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressLottie.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressLottie.isVisible = false
                    return false
                }

            })
            .thumbnail(0.05f)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.randomImageImageview)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}