package com.joseph.unsplash_mvvm.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.joseph.unsplash_mvvm.R
import com.joseph.unsplash_mvvm.adapters.PhotoAdapter
import com.joseph.unsplash_mvvm.databinding.FragmentSearchBinding
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.ui.detail.DetailActivity
import com.joseph.unsplash_mvvm.ui.main.SearchViewModel
import com.joseph.unsplash_mvvm.util.Resource
import com.joseph.unsplash_mvvm.util.setInfinityScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _query = ""
    private val adapter = PhotoAdapter()
    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectSearchResult()
        initializeRecyclerView()

        binding.imageSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                _query = query ?: return false
                viewModel.searchPhotos(_query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun initializeRecyclerView() = with(binding) {
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        photoRecyclerview.adapter = adapter
        photoRecyclerview.layoutManager = layoutManager
        photoRecyclerview.setInfinityScrollListener { viewModel.searchPhotos(_query) }
        adapter.setItemClickListener { photo, view ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("photo", photo)
            }
            startActivity(intent)
        }
    }

    private fun collectSearchResult() = lifecycleScope.launchWhenStarted {
        viewModel.photos.collect { state ->
            when (state) {
                is Resource.Success -> {
                    binding.progressLottie.isVisible = false
                    if (state.data?.results != null) {
                        if(viewModel.isSameQuery) {
                            adapter.submitList(adapter.currentList + state.data.results)
                        } else {
                            adapter.submitList(state.data.results)
                        }
                    }
                }
                is Resource.Loading -> {
                    binding.progressLottie.isVisible = true
                }
                is Resource.Error -> {
                    binding.progressLottie.isVisible = false
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}