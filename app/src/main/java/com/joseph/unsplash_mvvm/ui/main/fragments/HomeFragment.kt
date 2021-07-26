package com.joseph.unsplash_mvvm.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.joseph.unsplash_mvvm.R
import com.joseph.unsplash_mvvm.adapters.PhotoAdapter
import com.joseph.unsplash_mvvm.databinding.FragmentHomeBinding
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.ui.detail.DetailActivity
import com.joseph.unsplash_mvvm.ui.main.HomeViewModel
import com.joseph.unsplash_mvvm.ui.main.HomeViewModel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val natureAdapter: PhotoAdapter by lazy { PhotoAdapter() }
    private val animalAdapter: PhotoAdapter by lazy { PhotoAdapter() }

    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.natureRecyclerview.adapter = natureAdapter
        binding.natureRecyclerview.layoutManager = layoutManager
        natureAdapter.setItemClickListener { id ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        val layoutManager2 = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.animalRecyclerview.adapter = animalAdapter
        binding.animalRecyclerview.layoutManager = layoutManager2
        animalAdapter.setItemClickListener { id ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun collectRandomPhoto() {
        lifecycleScope.launchWhenStarted {
            viewModel.randomPhoto.collect { event ->
                event?.let {
                    when (it) {
                        is Event.LoadRandomPhotoEvent -> {
                            setRandomImage(it.data)
                        }
                        is Event.LoadRandomPhotoErrorEvent -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun collectRandomPhotoUser() {
        lifecycleScope.launchWhenStarted {
            viewModel.userProfile.collect { event ->
                event?.let {
                    when (it) {
                        is Event.LoadUserProfileEvent -> {
                            setRandomPhotoUserProfile(it.userProfile)
                        }
                        is Event.LoadUserProfileErrorEvent -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun collectNaturePhotos() {
        lifecycleScope.launchWhenStarted {
            viewModel.naturePhotos.collect { event ->
                event?.let {
                    if(it is Event.SearchPhotoEvent) {
                        Timber.d(it.photos.toString())
                        natureAdapter.submitList(it.photos)
                    } else if(it is Event.SearchPhotoErrorEvent) {
                        Timber.d(it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun collectAnimalPhotos() {
        lifecycleScope.launchWhenStarted {
            viewModel.animalPhotos.collect { event ->
                event?.let {
                    if(it is Event.SearchPhotoEvent) {
                        animalAdapter.submitList(it.photos)
                    } else if(it is Event.SearchPhotoErrorEvent) {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
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
            .thumbnail(0.05f)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.maintopicImgview)
    }
}