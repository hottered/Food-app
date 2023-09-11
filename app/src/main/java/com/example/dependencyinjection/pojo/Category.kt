package com.example.dependencyinjection.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cateogires")
data class Category(
    @PrimaryKey
    val idCategory: String,
    val strCategory: String?,
    val strCategoryDescription: String?,
    val strCategoryThumb: String?
)