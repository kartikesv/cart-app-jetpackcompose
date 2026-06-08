package com.mine.addtocartfeature.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val quantity: Int = 0
)
