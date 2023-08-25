package com.example.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CocktailEntity

@Dao
interface CocktailDao {

    @Query("SELECT * FROM table_cocktail")
    suspend fun getCocktails(): List<CocktailEntity>

    @Query("SELECT * FROM table_cocktail WHERE id=:id")
    suspend fun getCocktailById(id: Int): CocktailEntity

    @Query("DELETE FROM table_cocktail WHERE id=:id")
    suspend fun deleteCocktailById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cocktail: CocktailEntity)

    @Update
    suspend fun update(cocktail: CocktailEntity)
}