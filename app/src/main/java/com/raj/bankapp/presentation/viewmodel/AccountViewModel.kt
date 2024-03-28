package com.raj.bankapp.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.bankapp.domain.model.ItemType
import com.raj.bankapp.util.Resource
import com.raj.bankapp.presentation.base.UIState
import com.raj.bankapp.domain.usecase.GetAccountInfoUseCase
import com.raj.bankapp.domain.model.MainAccountListItem
import com.raj.bankapp.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
) : ViewModel() {

    private val _accountInfo = MutableStateFlow<UIState<List<MainAccountListItem>>>(UIState.Empty())
    val accountInfo = _accountInfo.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    fun getAccountData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // delay is introduced for demonstration purposes, to mimic the latency typically experienced in real-world API calls
            delay(2000)
            getAccountInfoUseCase.getAccountData()
                .flowOn(Dispatchers.IO)
                .catch { e -> e.printStackTrace() }
                .collectLatest {
                    with(_accountInfo) {
                        value = when (it) {
                            is Resource.Success -> {
                                _isRefreshing.value = false
                                UIState.Success(it.data.orEmpty())
                            }

                            is Resource.Error -> {
                                _isRefreshing.value = false
                                UIState.Error(it.message.orEmpty())
                            }
                        }
                    }

                }
        }
    }

    /**
     * Retrieves a Transaction object by its ID from the current account information state.
     */
    fun getTransactionById(transactionId: String): Transaction? {
        val currentState = _accountInfo.value
        if (currentState is UIState.Success) {
            return currentState.data.find { it.type == ItemType.TRANSACTION_ITEM && it.data is Transaction && it.data.id == transactionId }?.data as Transaction
        }
        return null
    }
}

