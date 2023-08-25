package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_cocktail")
data class CocktailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String?,
    val image: String? = null,
    val ingredients: List<String> = listOf(),
    val description: String? = null,
    val recipe: String? = null
)
