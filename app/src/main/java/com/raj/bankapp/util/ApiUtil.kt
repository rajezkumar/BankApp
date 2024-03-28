package com.raj.bankapp.util

import android.content.Context
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

object ApiUtil {

    fun readJsonFromFile(context: Context): String? {
        return try {
            context.assets.open("account.json")
                .bufferedReader().use {
                    return it.readText()
                }
        } catch (e: IOException) {
            null
        }
    }

    fun <T, O> parseResponse(response: Response<T>, mapper: (T?) -> O): Resource<O> {
        try {
            if (response.isSuccessful) {
                // success
                return Resource.Success(mapper(response.body()))
            } else {
                // error
                return Resource.Error(response.errorBody().toString())
            }
        } catch (e: Exception) {
            // error
            return Resource.Error(e.localizedMessage?.toString())
        }
    }

}