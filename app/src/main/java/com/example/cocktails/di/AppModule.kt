package com.example.cocktails.di

import com.example.cocktails.presentation.details.viewmodel.CocktailDetailsViewModel
import com.example.cocktails.presentation.edit.viewmodel.EditCocktailViewModel
import com.example.cocktails.presentation.home.viewmodel.MyCocktailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MyCocktailsViewModel(
            getCocktailsUseCase = get()
        )
    }

    viewModel {
        CocktailDetailsViewModel(
            getCocktailByIdUseCase = get()
        )
    }

    viewModel {
        EditCocktailViewModel(
            getCocktailByIdUseCase = get(),
            upsertCocktailsUseCase = get(),
            deleteCocktailByIdUseCase = get(),
            updateCocktailUseCase = get()
        )
    }
}