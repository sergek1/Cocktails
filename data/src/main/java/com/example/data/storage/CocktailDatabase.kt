package com.example.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.CocktailEntity

@Database(entities = [CocktailEntity::class], version = 1)
@TypeConverters(CocktailTypeConverters::class)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao
}