package com.example.livesportexercise

import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.network.CoroutineDispatcherProvider
import com.example.livesportexercise.network.LivesportRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import retrofit2.Response

@RunWith(JUnit4::class)
class SearchViewModelTest {
    lateinit var mainViewModel: SearchViewModel
    private val mainRepository = mock(LivesportRepository::class.java)
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mainViewModel = SearchViewModel(mainRepository, CoroutineDispatcherProvider())
    }

    @Test
    fun test_dataLoaded_Success_empty() {
        val sportIds = "1,2,3,4,5,6,7,8,9"
        val typeIds = "1,2,3,4"
        val query = "djo"
        runBlocking {
            `when`(mainRepository.getSearchResults(sportIds, typeIds, query))
                .thenReturn(Response.success(listOf<QueryItem>()))
            mainViewModel.getSearchResults(query, typeIds)
            testDispatcher.advanceTimeBy(1000)
            val (loadingState, resultState) = mainViewModel.uiState.take(2).toList()
            assertThat(loadingState).isEqualTo(SearchViewModel.SearchState.Loading)
            assertThat(resultState).isInstanceOf(SearchViewModel.SearchState.Loaded::class.java)
            verify(mainRepository).getSearchResults(sportIds, typeIds, query)
        }
    }

    @Test
    fun test_dataLoaded_Error_missingValues() {
        val sportIds = "1,2,3,4,5,6,7,8,9"
        val typeIds = "1,2,3,4"
        val query = ""
        runBlocking {
            val errorCode = 400
            `when`(mainRepository.getSearchResults(sportIds, typeIds, query))
                .thenReturn(
                Response.error(errorCode, ResponseBody.create("".toMediaTypeOrNull(), "")
                ))
            mainViewModel.getSearchResults(query, typeIds)
            testDispatcher.advanceTimeBy(1000)
            val (loadingState, resultState) = mainViewModel.uiState.take(2).toList()
            assertThat(loadingState).isEqualTo(SearchViewModel.SearchState.Loading)
            assertThat(resultState).isInstanceOf(SearchViewModel.SearchState.Error::class.java)
            verify(mainRepository).getSearchResults(sportIds, typeIds, query)
        }
    }

    @Test
    fun test_dataLoaded_Error_invalidValues() {
        val sportIds = "1,2,3,4,5,6,7,8,9"
        val typeIds = "1,2,3,4"
        val query = "q="
        runBlocking {
            val errorCode = 422
            `when`(mainRepository.getSearchResults(sportIds, typeIds, query))
                .thenReturn(
                    Response.error(errorCode, ResponseBody.create("".toMediaTypeOrNull(), "")
                    ))
            mainViewModel.getSearchResults(query, typeIds)
            testDispatcher.advanceTimeBy(1000)
            val (loadingState, resultState) = mainViewModel.uiState.take(2).toList()
            assertThat(loadingState).isEqualTo(SearchViewModel.SearchState.Loading)
            assertThat(resultState).isInstanceOf(SearchViewModel.SearchState.Error::class.java)
            verify(mainRepository).getSearchResults(sportIds, typeIds, query)
        }
    }

    @Test
    fun test_dataLoaded_Error_API() {
        val sportIds = "1,2,3,4,5,6,7,8,9"
        val typeIds = "1,2,3,4"
        val query = ""
        runBlocking {
            val errorCode = 503
            `when`(mainRepository.getSearchResults(sportIds, typeIds, query))
                .thenReturn(
                    Response.error(errorCode, ResponseBody.create("".toMediaTypeOrNull(), "")
                    ))
            mainViewModel.getSearchResults(query, typeIds)
            testDispatcher.advanceTimeBy(1000)
            val (loadingState, resultState) = mainViewModel.uiState.take(2).toList()
            assertThat(loadingState).isEqualTo(SearchViewModel.SearchState.Loading)
            assertThat(resultState).isInstanceOf(SearchViewModel.SearchState.Error::class.java)
            verify(mainRepository).getSearchResults(sportIds, typeIds, query)
        }
    }

    @Test
    fun test_dataLoaded_Error_Exception() {
        val sportIds = "1,2,3,4,5,6,7,8,9"
        val typeIds = "1,2,3,4"
        val query = "q="
        runBlocking {
            `when`(mainRepository.getSearchResults(sportIds, typeIds, query))
                .thenAnswer {
                    throw Exception("error")
                }
            mainViewModel.getSearchResults(query, typeIds)
            testDispatcher.advanceTimeBy(1000)
            val (loadingState, resultState) = mainViewModel.uiState.take(2).toList()
            assertThat(loadingState).isEqualTo(SearchViewModel.SearchState.Loading)
            assertThat(resultState).isInstanceOf(SearchViewModel.SearchState.Error::class.java)
            verify(mainRepository).getSearchResults(sportIds, typeIds, query)
        }
    }
}
