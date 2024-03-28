package com.raj.bankapp.domain.repository

import com.raj.bankapp.util.Resource
import com.raj.bankapp.domain.model.MainAccountListItem
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun getAccountData(): Flow<Resource<List<MainAccountListItem>>>
}