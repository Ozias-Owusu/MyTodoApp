package com.persol.mytodoapp.viewModels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PinScreenViewModel(context: Context) : ViewModel() {
    var pin by mutableStateOf("")
        private set

    private val sharedPreferences =
        context.getSharedPreferences("PinPrefs", Context.MODE_PRIVATE)

    init {
        pin = sharedPreferences.getString("pin", "") ?: ""
    }

    fun savePin(newPin: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("pin", newPin).apply()
            pin = newPin
        }
    }
    fun resetPin(newPin: String) {
        viewModelScope.launch {
            sharedPreferences.edit().remove("pin").apply()
            pin = newPin
        }
    }

    fun checkPin(enteredPin: String): Boolean {
        if (enteredPin.length == 4 && enteredPin.isDigitsOnly() && pin == "" ) {
            viewModelScope.launch {
             sharedPreferences.edit().putString("pin", enteredPin).apply()
             pin = enteredPin
            }
        }
        return enteredPin == pin
    }

}
