package com.example.data.wrapper

import com.example.data.model.CocktailEntity
import com.example.domain.model.Cocktail

fun List<CocktailEntity>.asDomainModel(): List<Cocktail> {
    return map {
        Cocktail(
            id = it.id,
            name = it.name,
            image = it.image,
            ingredients = it.ingredients,
            description = it.description,
            recipe = it.recipe
        )
    }
}

fun CocktailEntity.asDomainModel(): Cocktail {
    return Cocktail(
        id = id,
        name = name,
        image = image,
        ingredients = ingredients,
        description = description,
        recipe = recipe
    )
}

fun List<Cocktail>.asDataModel(): List<CocktailEntity> {
    return map {
        CocktailEntity(
            name = it.name,
            image = it.image,
            ingredients = it.ingredients,
            description = it.description,
            recipe = it.recipe
        )
    }
}

fun Cocktail.asDataModel(): CocktailEntity {
    return CocktailEntity(
        id = this.id,
        name = this.name,
        image = this.image,
        ingredients = this.ingredients,
        description = this.description,
        recipe = this.recipe
    )
}