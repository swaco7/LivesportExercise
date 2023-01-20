package com.example.livesportexercise

object Config {
    const val baseUrl = "https://s.livesport.services/api/v2/"
    const val baseImageUrl = "https://www.livesport.cz/res/image/data/"
    const val lang_id = 1
    const val project_id = 602
    const val project_type_id = 1

    const val allIds = "1,2,3,4"
    const val competitions = "1"
    const val participants = "2,3,4"

    val sportIds = (1..9).joinToString(",")
}