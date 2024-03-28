package com.raj.bankapp.data.api

import com.raj.bankapp.data.model.AccountInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Fetches account information using a dummy URL for demonstration.
 * This endpoint is not connected to a live backend but is intended to simulate API calls
 * and responses during the development phase.
 */

interface AccountApi {

    @GET("bank/{pathVal}/account.json")
    suspend fun getAccount(@Path("pathVal") pathVal: String): Response<AccountInfo>
}