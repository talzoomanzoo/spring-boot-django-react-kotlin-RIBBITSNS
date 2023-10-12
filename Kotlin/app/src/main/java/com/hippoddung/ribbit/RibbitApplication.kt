package com.hippoddung.ribbit

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hippoddung.ribbit.data.local.TokenManager
import com.hippoddung.ribbit.data.network.AuthRepository
import com.hippoddung.ribbit.network.AuthApiService
import com.hippoddung.ribbit.network.AuthAuthenticator
import com.hippoddung.ribbit.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@HiltAndroidApp
class RibbitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

