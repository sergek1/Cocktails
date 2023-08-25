package com.example.domain.usecase

import com.example.domain.model.Cocktail
import com.example.domain.repository.CocktailRepository

class UpdateCocktailUseCase(private val repository: CocktailRepository) {
    suspend operator fun invoke(cocktail: Cocktail) {
        return repository.update(cocktail)
    }
}