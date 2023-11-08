package com.example.reddittoppublication.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.reddittoppublication.R
import com.example.reddittoppublication.databinding.FragmentPostListBinding
import com.example.reddittoppublication.network.ApiHelperImpl
import com.example.reddittoppublication.network.Repository
import com.example.reddittoppublication.network.RetrofitBuilder
import com.example.reddittoppublication.viewmodel.DataViewModel
import com.example.reddittoppublication.windowpicture.WindowPictureFragment
import kotlinx.coroutines.launch

class PostListFragment : Fragment() {
    private val adapter by lazy {
        PostListRecyclerAdapter(onItemClicked, onLoadNextPage)
    }
    private val viewModel: DataViewModel by viewModels {
        DataViewModel.provideFactory(Repository(ApiHelperImpl(RetrofitBuilder.apiService)))
    }
    private val onItemClicked: (String) -> Unit = { item ->
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.apply {
            replace(R.id.fragment_main_view, WindowPictureFragment.newInstance(item))
            addToBackStack(null)
            commit()
        }
    }
    private val onLoadNextPage: () -> Unit = {
        viewModel.nextPage()
    }

    private var binding: FragmentPostListBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        listenerState()
    }

    private fun initUi() {
        binding?.apply {
            postList.adapter = adapter
        }
    }

    private fun listenerState() {
        lifecycleScope.launch {
            viewModel.state.collect { value ->
                adapter.submitList(value.data)
            }
        }

    }
}