package com.example.livesportexercise

import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.network.LivesportRepository
import com.example.livesportexercise.network.LivesportService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

class RepositoryTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val api = mock(LivesportService::class.java)
    private lateinit var repository: LivesportRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = LivesportRepository(api)
    }

    @Test
    fun test_GetSearch_Success() = runBlockingTest {
        val sportIds = "1,2,3"
        val typeIds = "1,2,3"
        val query = "djo"
        val expected = listOf<QueryItem>()
        `when`(api.search(1,602,1, sportIds, typeIds, query)).thenReturn(
            Response.success(expected)
        )
        val result = repository.getSearchResults(sportIds, typeIds, query)
        assertEquals(expected, result.body())
        verify(api).search(1,602,1,sportIds, typeIds, query)
    }

    @Test
    fun test_GetSearch_Error() = runBlockingTest {
        val sportIds = "1,2,3"
        val typeIds = "1,2,3"
        val query = ""
        val errorCode = 422
        `when`(api.search(1,602,1, sportIds, typeIds, query)).thenReturn(
            Response.error(errorCode, ResponseBody.create("".toMediaTypeOrNull(), ""))
        )
        val result = repository.getSearchResults(sportIds, typeIds, query)
        assertEquals(errorCode, result.code())
        verify(api).search(1,602,1, sportIds, typeIds, query)
    }
}