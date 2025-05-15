package com.gulaii.dto

data class FullMealInfo(
    val id: Int,
    val userId: String,
    val meal: String,
    val date: String,
    val time: String,
    val dishes: List<RequestMessage.Dish>
)