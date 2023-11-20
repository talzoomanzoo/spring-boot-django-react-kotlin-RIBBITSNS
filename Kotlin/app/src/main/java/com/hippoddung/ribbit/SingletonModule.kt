package com.hippoddung.ribbit

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hippoddung.ribbit.data.local.AuthManager
import com.hippoddung.ribbit.data.local.TokenManager
import com.hippoddung.ribbit.network.AuthApiService
import com.hippoddung.ribbit.network.AuthAuthenticator
import com.hippoddung.ribbit.network.AuthInterceptor
import com.hippoddung.ribbit.network.CLOUDINARY_URL
import com.hippoddung.ribbit.network.ChatApiService
import com.hippoddung.ribbit.network.CommuApiService
import com.hippoddung.ribbit.network.ListApiService
import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.StringApiService
import com.hippoddung.ribbit.network.UploadCloudinaryApiService
import com.hippoddung.ribbit.network.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ribbit_preferences")

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    @Singleton
    @Provides
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager = AuthManager(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthApiService =
        retrofit
            .build()
            .create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideRibbitAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): RibbitApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(RibbitApiService::class.java)

    @Singleton
    @Provides
    fun provideUserAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): UserApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(UserApiService::class.java)

    @Singleton
    @Provides
    fun provideListAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): ListApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(ListApiService::class.java)

    @Singleton
    @Provides
    fun provideCommuAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): CommuApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(CommuApiService::class.java)

    @Singleton
    @Provides
    fun provideChatAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): ChatApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(ChatApiService::class.java)

    @Singleton
    @Provides
    fun provideStringAPIService(okHttpClient: OkHttpClient): StringApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(StringApiService::class.java)

    @Singleton
    @Provides
    fun provideUploadCloudinaryApiService(): UploadCloudinaryApiService =
        Retrofit.Builder()
            .baseUrl(CLOUDINARY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(UploadCloudinaryApiService::class.java)
}