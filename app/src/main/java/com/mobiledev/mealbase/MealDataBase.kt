package com.mobiledev.mealbase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// initialization of abstract database
@Database(entities = [Meal :: class], version = 2)
abstract class MealDataBase : RoomDatabase() {

    // mealDao abstract fucntion
    abstract fun MealDao(): MealDao
    companion object {
        private var instance: MealDataBase? = null
        fun getInstance(context: Context): MealDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDataBase::class.java,
                    "mealDatabase"

                ).fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }

    }

}