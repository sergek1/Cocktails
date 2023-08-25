package com.example.cocktails.di

import androidx.room.Room
import com.example.data.repository.CocktailRepositoryImpl
import com.example.data.storage.CocktailDatabase
import com.example.domain.repository.CocktailRepository
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(get(), CocktailDatabase::class.java, "cocktail_database").build()
    }

    single {
        get<CocktailDatabase>().cocktailDao()
    }

    single {
        CocktailRepositoryImpl(dao = get()) as CocktailRepository
    }
}