package com.example.reddittoppublication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reddittoppublication.item.DataList
import com.example.reddittoppublication.network.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DataViewModel(private val repository: Repository) : ViewModel() {
    private val _state = MutableStateFlow(DataList())
    val state: StateFlow<DataList> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = repository.getPosts(null)
        }
    }

    fun nextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            val newState = repository.getPosts(_state.value.nextPage)
            _state.update { ui ->
                ui.copy(data = _state.value.data.plus(newState.data), nextPage = newState.nextPage)
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