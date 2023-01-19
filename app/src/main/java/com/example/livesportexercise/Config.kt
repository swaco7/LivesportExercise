package com.example.livesportexercise

object Config {
    val baseUrl = "https://s.livesport.services/api/v2/"
    val baseImageUrl = "https://www.livesport.cz/res/image/data/"
    val lang_id = 1
    val project_id = 602
    val project_type_id = 1

    val allIds = "1,2,3,4"
    val competitions = "1"
    val participants = "2,3,4"

    val sportIds = (1..9).joinToString(",")
}