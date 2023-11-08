package com.example.reddittoppublication.windowpicture

import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date

class WindowPictureViewModel(url: String?) : ViewModel() {
    private val _state = MutableStateFlow(url)
    val state: StateFlow<String?> = _state

    fun saveImageToStorage(activity: Activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            saveImage(activity)
        } else {
            requestPermissions(activity)
        }
    }

    private fun saveImage(activity: Activity) {
        val imageName = "reddit_image_${Date()}.jpg"
        var fos: OutputStream? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let {
                        resolver.openOutputStream(it)
                    }
                }
            } else {
                val imagesDirectory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDirectory, imageName)
                fos = FileOutputStream(image)
            }
            viewModelScope.launch(Dispatchers.IO) {
                fos?.use {
                    val bitmap: Bitmap =
                        Glide.with(activity.applicationContext).asBitmap().load(_state.value)
                            .submit(
                                Target.SIZE_ORIGINAL,
                                Target.SIZE_ORIGINAL
                            ).get()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

                }
            }
            Toast.makeText(activity.applicationContext, "Image saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(activity.applicationContext, "Image not saved", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
        )
    }

    companion object {
        fun provideFactory(url: String?): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WindowPictureViewModel(url) as T
                }
            }
    }
}