package com.example.data.repository

import com.example.data.storage.CocktailDao
import com.example.data.wrapper.asDataModel
import com.example.data.wrapper.asDomainModel
import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import com.example.domain.repository.CocktailRepository

class CocktailRepositoryImpl(
    private val dao: CocktailDao
) : CocktailRepository {
    override suspend fun getCocktails(): Result<List<Cocktail>> {
        return try {
            val result = dao.getCocktails()
            Result.Success(result.asDomainModel())
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }

    override suspend fun getCocktailById(id: Int): Result<Cocktail> {
        return try {
            val result = dao.getCocktailById(id)
            Result.Success(result.asDomainModel())
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }

    override suspend fun deleteCocktailById(id: Int) {
        dao.deleteCocktailById(id)
    }

    override suspend fun upsert(cocktail: Cocktail) {
        dao.upsert(cocktail.asDataModel())
    }

    override suspend fun update(cocktail: Cocktail) {
        dao.update(cocktail.asDataModel())
    }
}