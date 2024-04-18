package com.raj.bankapp.di

import android.content.Context
import com.raj.bankapp.data.api.AccountApi
import com.raj.bankapp.data.repository.AccountRespositoryImpl
import com.raj.bankapp.domain.MainAccountListItemMapper
import com.raj.bankapp.domain.repository.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext appContext: Context) =
        OkHttpClient.Builder().addInterceptor(interceptor = MockRequestInterceptor(appContext))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit
        .Builder()
        .client(okHttpClient)
        // The base URL is a dummy, as the actual responses are loaded from a local JSON.
        .baseUrl("https://bank.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AccountApi::class.java)

    @Provides
    @Singleton
    fun provideAccountRespository(
        accountApi: AccountApi,
        mainAccountListItemMapper: MainAccountListItemMapper
    ): AccountRepository =
        AccountRespositoryImpl(accountApi, mainAccountListItemMapper)

    @Provides
    @Singleton
    fun provideMainAccountListItemMapper(): MainAccountListItemMapper = MainAccountListItemMapper()

}
