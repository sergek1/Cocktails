package com.example.cocktails.di

import com.example.domain.usecase.DeleteCocktailByIdUseCase
import com.example.domain.usecase.GetCocktailByIdUseCase
import com.example.domain.usecase.GetCocktailsUseCase
import com.example.domain.usecase.UpdateCocktailUseCase
import com.example.domain.usecase.UpsertCocktailsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCocktailsUseCase(repository = get()) }
    factory { GetCocktailByIdUseCase(repository = get()) }
    factory { DeleteCocktailByIdUseCase(repository = get()) }
    factory { UpsertCocktailsUseCase(repository = get()) }
    factory { UpdateCocktailUseCase(repository = get()) }
}