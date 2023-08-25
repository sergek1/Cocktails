package com.example.domain.usecase

import com.example.domain.repository.CocktailRepository

class DeleteCocktailByIdUseCase(private val repository: CocktailRepository) {

    suspend operator fun invoke(id: Int) {
        return repository.deleteCocktailById(id)
    }

}