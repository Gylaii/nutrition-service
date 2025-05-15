package com.gulaii.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gulaii.dto.RequestMessage
import com.gulaii.dto.ResponseMessage
import com.gulaii.dto.SearchMealResponseBody
import com.gulaii.repository.TestRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import messaging.KeyDBClient
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single(createdAtStart = true)
class NutritionServiceRequestsListener(
    private val keyDbClient: KeyDBClient,
    private val testRepositoryImpl: TestRepositoryImpl,
    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        keyDbClient.sub(REQUESTS_CHANNEL) { message ->
            logger.info("Получено сообщение в Pub/Sub канале \"$REQUESTS_CHANNEL\": $message")
            when (val requestDto = objectMapper.readValue<RequestMessage>(message)) {
                is RequestMessage.SearchMeal -> processSearchMealRequest(requestDto)
                is RequestMessage.SaveMeal -> processSaveMealRequest(requestDto)
                is RequestMessage.GetMeal -> processGetMealRequest(requestDto)
            }
        }
    }

    private fun processGetMealRequest(requestDto: RequestMessage.GetMeal) {
        val response = testRepositoryImpl.getAllMealsWithDishes()

        keyDbClient.pub(
            RESPONSE_CHANNEL,
            objectMapper.writeValueAsString(
                ResponseMessage.SearchMeal(
                    correlationId = requestDto.correlationId,
                    payload = objectMapper.writeValueAsString(response),
                )
            ),
        )
    }

    private fun processSaveMealRequest(requestDto: RequestMessage.SaveMeal) {
        testRepositoryImpl.saveMeal(requestDto)

        keyDbClient.pub(
            RESPONSE_CHANNEL,
            objectMapper.writeValueAsString(
                ResponseMessage.SearchMeal(
                    correlationId = requestDto.correlationId,
                    payload = "",
                )
            ),
        )
    }

    private fun processSearchMealRequest(requestDto: RequestMessage.SearchMeal) {
        val response = runBlocking {
            httpClient.post(urlString = "https://www.foodrepo.org/api/v3/products/_search") {
                setBody(
                    """
                    {
                      "_source": {
                        "includes": [
                          "name_translations",
                          "nutrients.energy_calories_kcal",
                          "nutrients.protein",
                          "nutrients.fat",
                          "nutrients.carbohydrates",
                          "images"
                        ]
                      },
                      "size": ${requestDto.pageSize},
                      "from": ${requestDto.page * requestDto.pageSize},
                      "query": {
                        "query_string": {
                          "fields": [
                            "name_translations.en"
                          ],
                          "query": "${requestDto.searchTerm}~"
                        }
                      }
                    }
                """.trimIndent()
                )
                headers.apply {
                    append("Authorization", "Token token=\"$API_TOKEN\"")
                }

            }.bodyAsText().let { objectMapper.readValue<SearchMealResponseBody>(it) }
        }

        keyDbClient.pub(
            RESPONSE_CHANNEL,
            objectMapper.writeValueAsString(
                ResponseMessage.SearchMeal(
                    correlationId = requestDto.correlationId,
                    payload = objectMapper.writeValueAsString(
                        ResponseMessage.SearchMealData(
                            total = response.hits.total,
                            data = response.hits.hits,
                        )
                    ),
                )
            ),
        )
    }

    companion object {
        const val REQUESTS_CHANNEL = "nutrition-service:request-channel"
        const val RESPONSE_CHANNEL = "nutrition-service:response-channel"
        val API_TOKEN: String = System.getenv("OPENFOOD_REPO_API_KEY")
    }
}