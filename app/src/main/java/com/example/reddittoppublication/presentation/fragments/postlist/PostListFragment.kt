package com.example.reddittoppublication.presentation.fragments.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.reddittoppublication.R
import com.example.reddittoppublication.databinding.FragmentPostListBinding
import com.example.reddittoppublication.domain.model.ErrorStatus
import com.example.reddittoppublication.data.datasource.ApiHelperImpl
import com.example.reddittoppublication.domain.repository.Repository
import com.example.reddittoppublication.data.datasource.RetrofitBuilder
import com.example.reddittoppublication.presentation.fragments.windowpicture.WindowPictureFragment
import kotlinx.coroutines.launch

class PostListFragment : Fragment() {

    private val adapter by lazy {
        PostListRecyclerAdapter(
            onItemClicked = onItemClicked,
            onLoadNextPage = viewModel::nextPage
        )
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
        observerData()
        observerError()
    }

    private fun initUi() {
        binding?.apply {
            postList.adapter = adapter
        }
    }

    private fun observerData() {
        lifecycleScope.launch {
            viewModel.state.collect { value ->
                adapter.submitList(value.posts)
            }
        }
    }

    private fun observerError() {
        lifecycleScope.launch {
            viewModel.uiState.collect { error ->
                when (error) {
                    ErrorStatus.FAIL_CONNECT -> toastShow("Please turn on internet and restart app")
                    ErrorStatus.OTHER -> toastShow("Oops. Unexpected Error :-(")
                    ErrorStatus.NONE -> {}
                }
            }
        }
    }

    private fun toastShow(message: String) {
        binding?.apply {
            errorImg.visibility = View.VISIBLE
            postList.visibility = View.INVISIBLE
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}