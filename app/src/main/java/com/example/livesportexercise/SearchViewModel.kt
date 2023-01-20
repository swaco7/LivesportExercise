package com.example.livesportexercise

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.network.CoroutineDispatcherProvider
import com.example.livesportexercise.network.LivesportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: LivesportRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
): ViewModel() {
    private val _uiState = MutableStateFlow<SearchState>(SearchState.Empty)
    val uiState: StateFlow<SearchState> = _uiState

    fun getSearchResults(query: String, filter: String) {
        _uiState.value = SearchState.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response = repository.getSearchResults(sportIds = Config.sportIds, typeIds = filter, query = query)
                if (response.isSuccessful) {
                    _uiState.value =
                        SearchState.Loaded(response.body()?.toMutableList() ?: listOf())
                } else {
                    when (response.code()){
                        400 -> _uiState.value = SearchState.Error("400")
                        422 -> _uiState.value = SearchState.Error("422")
                        503 -> _uiState.value = SearchState.Error("503")
                        else -> _uiState.value = SearchState.Error("")
                    }
                }
            } catch (ex: Exception) {
                _uiState.value = SearchState.Error("503")
            }
        }
    }


    sealed class SearchState {
        object Empty : SearchState()
        object Loading : SearchState()
        class Loaded(val data: List<QueryItem>) : SearchState()
        class Error(val message: String) : SearchState()
    }

    fun resetLoadingState(){
        _uiState.value = SearchState.Empty
    }
}