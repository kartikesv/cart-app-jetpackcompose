package com.mine.addtocartfeature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mine.addtocartfeature.model.Product
import com.mine.addtocartfeature.repository.CartRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel: ViewModel(){

    //Dynamic product list
    //Now moved in repository
//    private val _product = MutableStateFlow(
//        List(15){
//            Product(
//                id = it,
//                name = "Product ${it + 1}",
//                description = "Description ${it +1 }"
//            )
//        }
//    )

    private val _product = MutableStateFlow<List<Product>>(emptyList())

    val product = _product.asStateFlow()

    fun getProducts(){
        viewModelScope.launch {
            val cartRepository = CartRepository()
            cartRepository.getProducts().collect {
                _product.value = it
            }
        }
    }

    private var _cartMessage = MutableSharedFlow<String>()

    val cartMessage = _cartMessage.asSharedFlow()
    fun cartButtonClick(){
        viewModelScope.launch {
            _cartMessage.emit("Go to carts clicked")
        }
    }

    // Total cart items
    val totalItems = _product
        .map { list->
            list.sumOf { it.quantity }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    /**
     * Increase item count
     */
    fun increase(productId: Int){
        _product.update { list->
            list.map {
                if(it.id == productId){
                    it.copy(quantity = it.quantity + 1)
                } else {
                    it
                }
            }
        }
    }

    /**
     * Decrease item count
     */
    fun decrease(productId: Int){
        _product.update { list->
            list.map {
                if(it.id == productId){
                    it.copy(
                        quantity = maxOf(0, it.quantity-1)
                    )
                } else{
                    it
                }
            }
        }
    }


    fun updateQuantity(productId: Int, value: String){
        val qunt = value.toIntOrNull() ?: 0

        _product.update { list->
            list.map {
                if(it.id == productId){
                    it.copy(quantity = qunt)
                } else {
                    it
                }
            }
        }

    }

}