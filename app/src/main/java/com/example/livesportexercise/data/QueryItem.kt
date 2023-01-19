package com.example.livesportexercise.data

import com.google.gson.annotations.SerializedName

data class QueryItem(
    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name: String,

    @SerializedName("sport")
    val sport: Sport,

    @SerializedName("defaultCountry")
    val country: Country,

    @SerializedName("images")
    val images: Array<Image>,

    @SerializedName("participantTypes")
    val participantTypes : Array<ParticipantType>,
)

data class ParticipantType(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)

