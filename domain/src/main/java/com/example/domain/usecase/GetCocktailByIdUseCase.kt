package com.example.domain.usecase

import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import com.example.domain.repository.CocktailRepository

class GetCocktailByIdUseCase(private val repository: CocktailRepository) {
    suspend operator fun invoke(id: Int): Result<Cocktail> {
        return repository.getCocktailById(id)
    }
}