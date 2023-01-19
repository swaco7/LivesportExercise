package com.example.livesportexercise.network

import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.data.QueryResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LivesportService {
    @GET("search")
    suspend fun search(
        @Query("lang-id") lang_id: Int,
        @Query("project-id") project_id: Int,
        @Query("project-type-id") project_type_id: Int,
        @Query("sport-ids") sport_ids: String,
        @Query("type-ids") type_ids: String,
        @Query("q") query: String
    ) : Response<List<QueryItem>>
}