package com.example.livesportexercise

import com.example.livesportexercise.data.*
import com.example.livesportexercise.network.LivesportService
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceTest {
    @Mock
    lateinit var mockWebServer: MockWebServer
    @Mock
    lateinit var service: LivesportService
    lateinit var gson: Gson
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        gson = GsonBuilder().create()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        service = retrofit.create(LivesportService::class.java)
    }

    @Test
    fun test_Search_SuccesEmpty() {
        runBlocking {
            val sportIds = "sportIds"
            val typeIds = "typeIds"
            val query = "search query"
            val expected = listOf<QueryItem>()
            mockWebServer.enqueue(MockResponse()
                .setBody(gson.toJson(expected))
                .setResponseCode(200))
            val response = service.search(1, 1, 1, sportIds, typeIds, query)
            val result = response.body()
            val responseCode = response.code()
            assertThat(responseCode).isEqualTo(200)
            assertThat(expected).isEqualTo(result)
        }
    }

    @Test
    fun test_Search_SuccesNonEmpty() {
        runBlocking {
            val sportIds = "sportIds"
            val typeIds = "typeIds"
            val query = "search query"
            val body = """
                [
                {
                "id": "AZg49Et9",
                "url": "djokovic-novak",
                "gender": {
                "id": 1,
                "name": "Men"
                },
                "name": "Djokovic Novak",
                "type": {
                "id": 3,
                "name": "Player"
                },
                "participantTypes": [
                {
                "id": 2,
                "name": "Player"
                }
                ],
                "sport": {
                "id": 2,
                "name": "Tennis"
                },
                "favouriteKey": {
                "web": "2_AZg49Et9",
                "portable": "2_AZg49Et9"
                },
                "flagId": null,
                "defaultCountry": {
                "id": 167,
                "name": "Serbia"
                },
                "images": [
                {
                "path": "tSfwGCdM-0rY6MEPI.png",
                "usageId": 3,
                "variantTypeId": 15
                }
                ],
                "teams": [],
                "defaultTournament": null,
                "superTemplate": null
                }
                ]
            """.trimIndent()
            val expected = listOf(QueryItem(
                id = "AZg49Et9",
                name = "Djokovic Novak",
                sport = Sport(id = "2", name = "Tennis"),
                country = Country(id = "167", name = "Serbia"),
                images = arrayOf(Image(path = "tSfwGCdM-0rY6MEPI.png")),
                participantTypes = arrayOf(ParticipantType(id = "2", name = "Player"))
            ))
            mockWebServer.enqueue(MockResponse()
                .setBody(body)
                .setResponseCode(200))
            val response = service.search(1, 1, 1, sportIds, typeIds, query)
            val result = response.body()
            val responseCode = response.code()
            assertThat(responseCode).isEqualTo(200)
            assertThat(expected[0].id).isEqualTo(result?.get(0)?.id)
            assertThat(expected[0].name).isEqualTo(result?.get(0)?.name)
        }
    }

    @Test
    fun test_Search_Error400() {
        runBlocking {
            val sportIds = "sportIds"
            val typeIds = "typeIds"
            val query = ""
            val expected = listOf<QueryItem>()
            mockWebServer.enqueue(MockResponse()
                .setBody(gson.toJson(expected))
                .setResponseCode(400))
            val response = service.search(1, 1, 1, sportIds, typeIds, query)
            val result = response.body()
            val responseCode = response.code()
            assertThat(responseCode).isEqualTo(400)
            assertThat(result).isNull()
        }
    }



    @Test
    fun test_Search_Error422() {
        runBlocking {
            val sportIds = "sportIds"
            val typeIds = "typeIds"
            val query = "q="
            val expected = listOf<QueryItem>()
            mockWebServer.enqueue(MockResponse()
                .setBody(gson.toJson(expected))
                .setResponseCode(422))
            val response = service.search(1, 1, 1, sportIds, typeIds, query)
            val result = response.body()
            val responseCode = response.code()
            assertThat(responseCode).isEqualTo(422)
            assertThat(result).isNull()
        }
    }

    @After
    fun cleanup(){
        mockWebServer.shutdown()
    }
}