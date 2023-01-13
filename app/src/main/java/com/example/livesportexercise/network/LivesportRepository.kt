package com.example.livesportexercise.network

import com.example.livesportexercise.Config
import com.example.livesportexercise.data.QueryItem
import com.example.livesportexercise.data.QueryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LivesportRepository(
    private val livesportService : LivesportService
) {
    suspend fun getSearchResults(sportIds: String, typeIds: String, query: String): List<QueryItem> =
        livesportService.search(
            lang_id = Config.lang_id,
            project_id = Config.project_id,
            project_type_id = Config.project_type_id,
            sport_ids = sportIds,
            type_ids = typeIds,
            query = query
        )
}