package com.example.reddittoppublication.presentation.fragments.windowpicture

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.reddittoppublication.databinding.FragmentWindowPictureBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.Serializable
import java.util.Date


class WindowPictureFragment : Fragment() {

    private val url by lazy { (arguments?.getSerializable(URL_ARG_PARAM) ?: "") as List<String> }
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
    }

    private fun initUi() {
        binding?.apply {
            if (url[0].endsWith(".gif")) {
                saveButton.isVisible = false
            }
            carouselImage.setImageListener { position, imageView ->
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                imageView.adjustViewBounds = true
                Glide.with(requireContext())
                    .load(url[position])
                    .centerInside()
                    .into(imageView)
            }
            saveButton.setOnClickListener {
                saveImageToStorage(url[carouselImage.currentItem])
            }
            carouselImage.pageCount = url.size
        }

    }

    private fun saveImageToStorage(image: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) saveImage(image)
        else requestPermissions()
    }

    private fun saveImage(image: String) {
        val imageName = "reddit_image_${Date()}.jpg"
        var fos: OutputStream? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requireActivity().contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let(resolver::openOutputStream)
                }
            } else {
                val imagesDirectory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDirectory, imageName)
                fos = FileOutputStream(image)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                fos?.use { outStream ->
                    val bitmap: Bitmap = Glide.with(requireContext())
                        .asBitmap()
                        .load(image)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                }
            }
            Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Image not saved", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
        )
    }

    companion object {

        private const val URL_ARG_PARAM = "url"

        fun newInstance(url: List<String>) = WindowPictureFragment().apply {
            arguments = Bundle().apply {
                putSerializable(URL_ARG_PARAM, url as Serializable)
            }
        }
    }
}