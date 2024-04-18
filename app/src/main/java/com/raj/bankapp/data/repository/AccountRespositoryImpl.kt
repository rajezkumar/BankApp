package com.raj.bankapp.data.repository

import com.raj.bankapp.domain.MainAccountListItemMapper
import com.raj.bankapp.util.ApiUtil
import com.raj.bankapp.util.Resource
import com.raj.bankapp.data.api.AccountApi
import com.raj.bankapp.domain.repository.AccountRepository
import com.raj.bankapp.domain.model.MainAccountListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountRespositoryImpl @Inject constructor(
    private val accountApi: AccountApi,
    private val mapper: MainAccountListItemMapper
) : AccountRepository {
    override suspend fun getAccountData(): Flow<Resource<List<MainAccountListItem>>> = flow {
        val accountInfo = ApiUtil.parseResponse(accountApi.getAccount("")) {
            mapper.map(it)
        }
        emit(accountInfo)
    }

}
