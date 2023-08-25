package com.example.cocktails.presentation.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import com.example.domain.usecase.GetCocktailByIdUseCase

class CocktailDetailsViewModel(
    private val getCocktailByIdUseCase: GetCocktailByIdUseCase
) : ViewModel() {
    var cocktail: LiveData<Result<Cocktail>> = liveData(context = viewModelScope.coroutineContext) {
        emit(Result.Loading())
    }

    fun getCocktail(id: Int) {
        cocktail = liveData(context = viewModelScope.coroutineContext) {
            emit(Result.Loading())
            val result = getCocktailByIdUseCase(id)
            emit(result)
        }
    }
}