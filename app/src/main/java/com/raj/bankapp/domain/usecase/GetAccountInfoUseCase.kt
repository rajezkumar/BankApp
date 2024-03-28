package com.raj.bankapp.domain.usecase

import com.raj.bankapp.util.Resource
import com.raj.bankapp.domain.repository.AccountRepository
import com.raj.bankapp.domain.model.MainAccountListItem
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun getAccountData(): Flow<Resource<List<MainAccountListItem>>> =
        accountRepository.getAccountData()
}