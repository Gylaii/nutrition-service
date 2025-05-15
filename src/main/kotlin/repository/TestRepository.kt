package com.gulaii.repository

import com.gulaii.dto.FullMealInfo
import com.gulaii.dto.RequestMessage
import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Singleton
import java.sql.Connection
import javax.sql.DataSource
import kotlin.use

interface TestRepository {
    fun initialize()
}

@Singleton(binds = [TestRepository::class], createdAtStart = true)
class TestRepositoryImpl(@Qualifier(name = "pgDataSource") val ds: DataSource) : TestRepository {
    override fun initialize() {
        ds.connection.use {
            it.prepareStatement("""
                CREATE TABLE IF NOT EXISTS CITIES (
                    ID SERIAL PRIMARY KEY, 
                    NAME VARCHAR(255), 
                    POPULATION INT
                );
                """.trimIndent()
            ).execute()
            it.prepareStatement("""
                CREATE TABLE IF NOT EXISTS meals (
                    id SERIAL PRIMARY KEY,
                    user_id VARCHAR NOT NULL,
                    meal VARCHAR NOT NULL,
                    date VARCHAR NOT NULL,
                    time VARCHAR NOT NULL
                );
                """.trimIndent()
            ).execute()
            it.prepareStatement("""
                CREATE TABLE dishes (
                    id SERIAL PRIMARY KEY,
                    meal_id INTEGER NOT NULL REFERENCES meals(id) ON DELETE CASCADE,
                    name VARCHAR NOT NULL,
                    weight DOUBLE PRECISION NOT NULL,
                    calory DOUBLE PRECISION NOT NULL
                );
                """.trimIndent()
            ).execute()
        }
    }

    fun saveMeal(meal: RequestMessage.SaveMeal) {
        ds.connection.use { conn ->
            conn.autoCommit = false
            try {
                val mealId = insertMeal(conn, meal.userId, meal.meal, meal.date, meal.time)
                insertDishes(conn, mealId, meal.dishes)
                conn.commit()
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    }
    fun getAllMealsWithDishes(): List<FullMealInfo> {
        val meals = mutableListOf<FullMealInfo>()
        val conn = ds.connection

        val mealSql = "SELECT id, user_id, meal, date, time FROM meals"
        val dishSql = "SELECT name, weight, calory FROM dishes WHERE meal_id = ?"

        conn.use { connection ->
            connection.prepareStatement(mealSql).use { mealStmt ->
                val rsMeal = mealStmt.executeQuery()
                while (rsMeal.next()) {
                    val mealId = rsMeal.getInt("id")
                    val userId = rsMeal.getString("user_id")
                    val meal = rsMeal.getString("meal")
                    val date = rsMeal.getString("date")
                    val time = rsMeal.getString("time")

                    val dishes = mutableListOf<RequestMessage.Dish>()
                    connection.prepareStatement(dishSql).use { dishStmt ->
                        dishStmt.setInt(1, mealId)
                        val rsDish = dishStmt.executeQuery()
                        while (rsDish.next()) {
                            dishes.add(
                                RequestMessage.Dish(
                                    name = rsDish.getString("name"),
                                    weight = rsDish.getDouble("weight"),
                                    calory = rsDish.getDouble("calory")
                                )
                            )
                        }
                    }

                    meals.add(
                        FullMealInfo(
                            id = mealId,
                            userId = userId,
                            meal = meal,
                            date = date,
                            time = time,
                            dishes = dishes
                        )
                    )
                }
            }
        }

        return meals
    }

    private fun insertMeal(conn: Connection, userId: String, meal: String, date: String, time: String): Int {
        val sql = "INSERT INTO meals (user_id, meal, date, time) VALUES (?, ?, ?, ?) RETURNING id"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, userId)
            stmt.setString(2, meal)
            stmt.setString(3, date)
            stmt.setString(4, time)
            stmt.executeQuery().use { rs ->
                if (rs.next()) {
                    return rs.getInt("id")
                } else {
                    throw IllegalStateException("Meal insert failed, no ID returned")
                }
            }
        }
    }

    private fun insertDishes(conn: Connection, mealId: Int, dishes: List<RequestMessage.Dish>) {
        val sql = "INSERT INTO dishes (meal_id, name, weight, calory) VALUES (?, ?, ?, ?)"
        conn.prepareStatement(sql).use { stmt ->
            for (dish in dishes) {
                stmt.setInt(1, mealId)
                stmt.setString(2, dish.name)
                stmt.setDouble(3, dish.weight)
                stmt.setDouble(4, dish.calory)
                stmt.addBatch()
            }
            stmt.executeBatch()
        }
    }
}