package com.example.reddittoppublication.windowpicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.reddittoppublication.databinding.FragmentWindowPictureBinding
import kotlinx.coroutines.launch

private const val ARG_PARAM = "url"

class WindowPictureFragment : Fragment() {
    private val windowPictureViewModel: WindowPictureViewModel by viewModels {
        var url: String? = null
        arguments?.let {
            url = it.getString(ARG_PARAM)
        }
        WindowPictureViewModel.provideFactory(url ?: "")
    }

    private var binding: FragmentWindowPictureBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentWindowPictureBinding.inflate(LayoutInflater.from(context), container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        downloadPicture()
    }


    private fun initUi() {
        lifecycleScope.launch {
            windowPictureViewModel.state.collect { url ->
                binding?.let {
                    Glide.with(requireActivity().applicationContext).load(url)
                        .into(it.fullscreenImage)
                }
            }
        }

    }

    private fun downloadPicture() {
        binding?.imageButton?.setOnClickListener {
            windowPictureViewModel.saveImageToStorage(requireActivity())
        }
    }

    companion object {
        fun newInstance(url: String) = WindowPictureFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM, url)
            }
        }
    }
}