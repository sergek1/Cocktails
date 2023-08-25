package com.example.domain.usecase

import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import com.example.domain.repository.CocktailRepository

class GetCocktailsUseCase(private val repository: CocktailRepository) {
    suspend operator fun invoke(): Result<List<Cocktail>> {
        return repository.getCocktails()
    }
}