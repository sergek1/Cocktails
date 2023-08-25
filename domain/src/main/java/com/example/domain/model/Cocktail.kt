package com.example.domain.model

data class Cocktail(
    var id: Int? = null,
    var name: String?,
    var image: String? = null,
    var ingredients: List<String> = listOf(),
    var description: String?,
    var recipe: String?
)
