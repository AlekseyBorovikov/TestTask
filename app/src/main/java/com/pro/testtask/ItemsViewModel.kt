package com.pro.testtask

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pro.testtask.remote.data.ItemRemote
import com.pro.testtask.repository.TaskRepository
import com.pro.testtask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _itemIndexes = mutableStateOf<List<Int>>(listOf())
    val itemIndexes: State<List<Int>> get() = _itemIndexes
    private val _currentItem = mutableStateOf<ItemRemote?>(null)
    val currentItem: State<ItemRemote?> get() = _currentItem
    var currentItemPosition = 0
        private set

    fun nextCurrentItemPosition() {
        currentItemPosition = (currentItemPosition + 1) % _itemIndexes.value.size
    }
    // Index loading error text
    private val _loadIndexesError = mutableStateOf("")
    val loadIndexesError: State<String> get() = _loadIndexesError
    // Index loading flag
    private val _isIndexesLoading = mutableStateOf(false)
    val isIndexesLoading: State<Boolean> get() = _isIndexesLoading
    // Item loading error text
    private val _loadItemError = mutableStateOf("")
    val loadItemError: State<String> get() = _loadItemError
    // Item loading flag
    private val _isItemLoading = mutableStateOf(false)
    val isItemLoading: State<Boolean> get() = _isItemLoading

    // Loading indexes immediately after application launch
    init {
        Log.d("ViewModel", "init")
        loadIndexes()
    }

    // Loading indexes
    fun loadIndexes() = viewModelScope.launch {
        _isIndexesLoading.value = true
        _loadIndexesError.value = ""
        val result = repository.getIndexes()
        when(result){
            is Resource.Success -> {
                result.data?.let { indexes ->
                    _itemIndexes.value = indexes
                }
                _loadIndexesError.value = ""
                _isIndexesLoading.value = false
            }
            is Resource.Error -> {
                _loadIndexesError.value = result.message!!
                _isIndexesLoading.value = false
            }
        }
    }

    // Item loading
    fun loadItem(itemId: Int) = viewModelScope.launch {

        Log.d("ViewModel", "getItem")
        _isItemLoading.value = true
        _loadItemError.value = ""
        val result = repository.getItem(itemId)
        when(result){
            is Resource.Success -> {
                result.data?.let { item ->
                    _currentItem.value = item
                }
                _loadItemError.value = ""
                _isItemLoading.value = false
                Log.d("ViewModel", "getItem SUCCESS: ${currentItem.value}")
            }
            is Resource.Error -> {
                _loadItemError.value = result.message!!
                _isItemLoading.value = false
                Log.d("ViewModel", "getItem ERROR")
            }
        }
    }

    fun updateCurrentItem() {
        itemIndexes.value.getOrNull(currentItemPosition)?.let { itemId ->
            loadItem(itemId)
        }
    }
}