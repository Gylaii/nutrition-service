package com.gulaii.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SearchMealResponseBody(
    @JsonProperty("hits")
    val hits: HitsMeta,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HitsMeta(
    @JsonProperty("total")
    val total: Int,
    @JsonProperty("hits")
    val hits: List<Hit>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Hit(
    @JsonProperty("_source")
    val source: Source
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Source(
    @JsonProperty("images")
    val images: List<Image>,

    @JsonProperty("name_translations")
    val nameTranslations: Map<String, String>,

    @JsonProperty("nutrients")
    val nutrients: Nutrients?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Image(
    @JsonProperty("large")
    val large: String,
    @JsonProperty("categories")
    val categories: List<String>,
    @JsonProperty("thumb")
    val thumb: String,
    @JsonProperty("xlarge")
    val xlarge: String,
    @JsonProperty("medium")
    val medium: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Nutrients(
    @JsonProperty("fat")
    val fat: NutrientInfo,

    @JsonProperty("carbohydrates")
    val carbohydrates: NutrientInfo,

    @JsonProperty("protein")
    val protein: NutrientInfo,

    @JsonProperty("energy_calories_kcal")
    val kcal: NutrientInfo,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NutrientInfo(
    @JsonProperty("per_hundred")
    val perHundred: Float,

    @JsonProperty("unit")
    val unit: String,

    @JsonProperty("per_portion")
    val perPortion: Float,
)
