package com.example.livesportexercise

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.network.LivesportRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val repository: LivesportRepository): ViewModel(){
    private val _uiState = MutableStateFlow<SearchState>(SearchState.Empty)
    val uiState: StateFlow<SearchState> = _uiState

    fun getSearchResults() {
        viewModelScope.launch {
            try {
                val response = repository.getSearchResults(sportIds = "1,2,3", typeIds = "1,2,3,4", query = "djo")
                _uiState.value = SearchState.Loaded(response.toMutableList() ?: listOf())
            } catch (ex: Exception) {
                Log.e("exception", ex.toString())
                throw ex
            }
        }
    }

    sealed class SearchState {
        object Empty : SearchState()
        object Loading : SearchState()
        class Loaded(val data: List<QueryItem>) : SearchState()
        class Error(val message: String) : SearchState()
    }
}