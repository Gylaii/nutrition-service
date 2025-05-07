package com.gulaii.dto

import com.fasterxml.jackson.annotation.JsonProperty
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
    abstract val type: String

    data class SearchMeal(
        @JsonProperty("correlation_id")
        val correlationId: String,
        val total: Int,
        val data: List<Hit>,
    ): ResponseMessage() {
        override val type: String = "SEARCH_MEAL"
    }
}