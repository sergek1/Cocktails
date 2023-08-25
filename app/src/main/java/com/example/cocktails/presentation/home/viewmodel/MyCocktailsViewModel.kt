package com.example.cocktails.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import com.example.domain.usecase.GetCocktailsUseCase

class MyCocktailsViewModel(
    private val getCocktailsUseCase: GetCocktailsUseCase
) : ViewModel() {
    val items: LiveData<Result<List<Cocktail>>> =
        liveData(context = viewModelScope.coroutineContext) {
            emit(Result.Loading())
            val result = getCocktailsUseCase()
            emit(result)
        }
}