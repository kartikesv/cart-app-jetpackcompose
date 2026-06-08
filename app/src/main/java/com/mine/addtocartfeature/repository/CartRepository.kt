package com.mine.addtocartfeature.repository

import com.mine.addtocartfeature.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CartRepository {

    fun getProducts(): Flow<List<Product>> = flow {
        emit(
            List(10) { index ->
                Product(
                    id = index,
                    name = "Product ${index + 1}",
                    description = "Description ${index + 1}"
                )
            }
        )
    }
}