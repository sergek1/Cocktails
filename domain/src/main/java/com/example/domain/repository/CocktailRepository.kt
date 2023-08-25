package com.example.domain.repository

import com.example.domain.model.Cocktail
import com.example.domain.model.Result

interface CocktailRepository {
    suspend fun getCocktails(): Result<List<Cocktail>>
    suspend fun getCocktailById(id: Int): Result<Cocktail>
    suspend fun deleteCocktailById(id: Int)
    suspend fun upsert(cocktail: Cocktail)
    suspend fun update(cocktail: Cocktail)
}