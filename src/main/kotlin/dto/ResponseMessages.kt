package com.gulaii.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ResponseMessage.SearchMeal::class, name = "SEARCH_MEAL"),
)
sealed class ResponseMessage {
    abstract val correlationId: String
    abstract val payload: String

    data class SearchMeal(
        override val correlationId: String,
        override val payload: String,
    ): ResponseMessage()

    data class SearchMealData(
        val total: Int,
        val data: List<Hit>,
        val type: String = "SEARCH_MEAL"
    )
}