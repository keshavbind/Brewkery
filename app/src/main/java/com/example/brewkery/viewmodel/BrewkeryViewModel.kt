package com.example.brewkery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.data.model.MenuResponse
import com.example.brewkery.repository.BrewkeryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrewkeryViewModel : ViewModel() {
    private val repository = BrewkeryRepository()

    private val _menu = MutableStateFlow<MenuResponse?>(null)
    val menu: StateFlow<MenuResponse?> = _menu

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadMenu() {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.getMenu()
                _menu.value = response

            } catch (e: Exception) {
                _error.value = e.message ?: "Something went wrong"

            } finally {
                _isLoading.value = false
            }
        }
    }
}