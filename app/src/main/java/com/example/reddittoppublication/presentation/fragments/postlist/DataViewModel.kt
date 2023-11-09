package com.example.reddittoppublication.presentation.fragments.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reddittoppublication.domain.model.PostPage
import com.example.reddittoppublication.domain.model.ErrorStatus
import com.example.reddittoppublication.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class DataViewModel(private val repository: Repository) : ViewModel() {

    private val _state = MutableStateFlow(PostPage())
    private val _uiState = MutableStateFlow(ErrorStatus.NONE)
    val uiState: StateFlow<ErrorStatus> = _uiState
    val state: StateFlow<PostPage> = _state

    init {
        nextPage(isInitial = true)
    }

    fun nextPage(isInitial: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newState = repository.getPosts(if (isInitial) null else _state.value.nextPage)
                _state.update { ui ->
                    ui.copy(
                        posts = if (isInitial) {
                            newState.posts.map { post ->
                                post.copy(content = post.content)
                            }
                        } else {
                            _state.value.posts.plus(
                                newState.posts.map { post ->
                                    post.copy(content = post.content)
                                }
                            )
                        },
                        nextPage = newState.nextPage
                    )
                }
            } catch (ex: Exception) {
                uiStateErrorStatus(ex)
            }
        }
    }

    private fun uiStateErrorStatus(ex: Exception) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                when (ex) {
                    is UnknownHostException -> ErrorStatus.FAIL_CONNECT
                    else -> ErrorStatus.OTHER
                }
            }
        }
    }

    companion object {
        fun provideFactory(repository: Repository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DataViewModel(repository) as T
                }
            }
    }
}