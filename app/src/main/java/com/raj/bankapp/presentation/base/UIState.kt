package com.raj.bankapp.presentation.base

sealed class UIState<T> {
    class Success<T>(val data: T) : UIState<T>()
    class Error<T>(val errorMessage: String) : UIState<T>()
    class Empty<T> : UIState<T>()
}
