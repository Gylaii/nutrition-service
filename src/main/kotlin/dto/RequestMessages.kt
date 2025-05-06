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
    JsonSubTypes.Type(value = RequestMessage.SearchMeal::class, name = "SEARCH_MEAL"),
)
sealed class RequestMessage {
    abstract val type: String
    abstract val correlationId: String

    data class SearchMeal(
        @JsonProperty("search_term")
        val searchTerm: String,
        @JsonProperty("page")
        val page: Int,
        @JsonProperty("page_size")
        val pageSize: Int,
        @JsonProperty("correlation_id")
        override val correlationId: String,
    ) : RequestMessage() {
        override val type = "SEARCH_MEAL"
    }
}