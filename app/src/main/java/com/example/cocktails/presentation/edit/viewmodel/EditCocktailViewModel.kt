package com.example.cocktails.presentation.edit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import com.example.domain.usecase.DeleteCocktailByIdUseCase
import com.example.domain.usecase.GetCocktailByIdUseCase
import com.example.domain.usecase.UpdateCocktailUseCase
import com.example.domain.usecase.UpsertCocktailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditCocktailViewModel(
    private val getCocktailByIdUseCase: GetCocktailByIdUseCase,
    private val upsertCocktailsUseCase: UpsertCocktailsUseCase,
    private val deleteCocktailByIdUseCase: DeleteCocktailByIdUseCase,
    private val updateCocktailUseCase: UpdateCocktailUseCase
) : ViewModel() {
    var cocktail: LiveData<Result<Cocktail>> = liveData(context = viewModelScope.coroutineContext) {
        emit(Result.Loading())
    }

    fun getCocktail(id: Int?) {
        cocktail = liveData(context = viewModelScope.coroutineContext) {
            if (id == null) {
                emit(
                    Result.Success(
                        Cocktail(
                            name = null,
                            description = null,
                            recipe = null,
                            image = null,
                            ingredients = listOf()
                        )
                    )
                )
            } else {
                emit(Result.Loading())
                val result = getCocktailByIdUseCase(id)
                emit(result)
            }
        }
    }

    fun saveCocktail(cocktail: Cocktail, isNew: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNew) {
                upsertCocktailsUseCase(cocktail)
            } else {
                updateCocktailUseCase(cocktail)
            }
        }
    }

    fun deleteCocktail(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                if (id != null) {
                    deleteCocktailByIdUseCase(id)
                }
            }
        }
    }
}