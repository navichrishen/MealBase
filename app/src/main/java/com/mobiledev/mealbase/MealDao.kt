package com.mobiledev.mealbase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// meal dao interface
@Dao
interface MealDao {

    // query to select all from meal table
    @Query("SELECT * FROM meal_table")
    fun getAll(): List<Meal>


    //@Query("SELECT * FROM meal_table WHERE meal_name LIKE '%' || :searchMeal || '%' OR ingredient1 OR ingredient2 OR ingredient3 OR ingredient4 OR ingredient5 OR ingredient6 OR ingredient7 OR ingredient8 OR ingredient9 OR ingredient10 OR ingredient11 OR ingredient12 OR ingredient13 OR ingredient14 OR ingredient15 OR ingredient16 OR ingredient17 OR ingredient18 OR ingredient19 OR ingredient20 Like '%'  || :searchMeal || '%'")
    @Query("SELEcT * FROM meal_table WHERE meal_name LIKE '%' || :searchMeal || '%' OR ingredient1 LIKE '%'  || :searchMeal || '%' OR ingredient2 LIKE '%'  || :searchMeal || '%' OR ingredient3 LIKE '%' || :searchMeal || '%' OR ingredient4 LIKE '%' || :searchMeal || '%' OR ingredient5 LIKE '%'  || :searchMeal || '%'OR ingredient6 LIKE '%'  || :searchMeal || '%' OR ingredient7 LIKE '%'  || :searchMeal || '%' OR ingredient8 LIKE '%'  || :searchMeal || '%' OR ingredient9 LIKE '%'  || :searchMeal || '%' OR ingredient10 LIKE '%'  || :searchMeal || '%' OR ingredient11 LIKE '%'  || :searchMeal || '%' OR ingredient12 LIKE '%'  || :searchMeal || '%' OR ingredient13 LIKE '%'  || :searchMeal || '%' OR ingredient14 LIKE '%'  || :searchMeal || '%' OR ingredient15 LIKE '%'  || :searchMeal || '%' OR ingredient16 LIKE '%'  || :searchMeal || '%' OR ingredient17 LIKE '%'  || :searchMeal || '%' OR ingredient18 LIKE '%'  || :searchMeal || '%' OR ingredient19 LIKE '%'  || :searchMeal || '%' OR ingredient20 LIKE '%'  || :searchMeal || '%' ")
    suspend fun searchMeals(searchMeal: String): List<Meal>

    // query to reject the entries on conflict
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(mealDetails: Meal)

    // query to insert all records at once
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg meal: Meal)






}