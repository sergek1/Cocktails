package com.example.domain.usecase

import com.example.domain.model.Cocktail
import com.example.domain.repository.CocktailRepository

class UpsertCocktailsUseCase(private val repository: CocktailRepository) {

    suspend operator fun invoke(cocktail: Cocktail) {
        return repository.upsert(cocktail)
    }
}