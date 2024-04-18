package com.raj.bankapp

import com.raj.bankapp.data.api.AccountApi
import com.raj.bankapp.data.model.Account
import com.raj.bankapp.data.model.AccountInfo
import com.raj.bankapp.data.model.Atm
import com.raj.bankapp.data.model.Location
import com.raj.bankapp.data.model.Transaction
import com.raj.bankapp.data.repository.AccountRespositoryImpl
import com.raj.bankapp.domain.MainAccountListItemMapper
import com.raj.bankapp.domain.model.AccountName
import com.raj.bankapp.domain.model.ItemType
import com.raj.bankapp.domain.model.MainAccountListItem
import com.raj.bankapp.util.Resource
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AccountRepositoryImplTest {

    private lateinit var accountRespositoryImpl: AccountRespositoryImpl

    @MockK
    private lateinit var mainAccountListItemMapper: MainAccountListItemMapper

    @Before
    fun setUp() {
        val accountApi = mockk<AccountApi>()
        coEvery { accountApi.getAccount("") } returns Response.success(
            AccountInfo(
                account = Account(
                    bsb = "062005",
                    accountNumber = "17095888",
                    balance = "246.7",
                    available = "226.76",
                    accountName = "Complete Access"
                ), atms = listOf(
                    Atm(
                        address = "8 Alfred St, Sydney, NSW 2000",
                        id = "129382",
                        location = Location(lat = -33.861382, lon = 151.210316),
                        name = "Circular Quay Station"
                    )
                ), transactions = listOf(
                    Transaction(
                        amount = "-2.49",
                        id = "0B40D0E2-3B68-4A5E-97BD-0E083CAD4123",
                        isPending = false,
                        description = "Google Storage (via Google) Sydney",
                        category = "uncategorised",
                        effectiveDate = "2021-02-18",
                        atmId = "129382"
                    )
                )
            )
        )
        accountRespositoryImpl = AccountRespositoryImpl(accountApi, mainAccountListItemMapper)
    }

    @Test
    fun `getAccountData returns success`() = runBlocking {
        val resultList = mutableListOf<Resource<List<MainAccountListItem>>>()
        accountRespositoryImpl.getAccountData().toList(resultList)
        val firstResult = resultList.firstOrNull()
        assertTrue(firstResult is Resource.Success)
        val data = (firstResult as? Resource.Success)?.data
        assertNotNull(data)
        val accountNameItem = data?.find { it.type == ItemType.ACCOUNT_HEADER }
        val accountName = (accountNameItem?.data as? AccountName)?.accountName
        assertEquals("Complete Access", accountName)
    }
}
