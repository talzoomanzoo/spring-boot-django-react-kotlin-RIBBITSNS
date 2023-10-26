package com.hippoddung.ribbit.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hippoddung.ribbit.dataStore
import com.hippoddung.ribbit.network.bodys.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthManager(
    private val context: Context
) {
    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PW_KEY = stringPreferencesKey("pw")
    }

    fun getEmail(): Flow<String?> {
        return context.dataStore.data.map{preferences ->
            preferences[EMAIL_KEY]
        }
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun deleteEmail(){
        context.dataStore.edit {preferences ->
            preferences.remove(EMAIL_KEY)
        }
    }

    fun getPW(): Flow<String?> {
        return context.dataStore.data.map{preferences ->
            preferences[PW_KEY]
        }
    }

    suspend fun savePW(pW: String) {
        context.dataStore.edit { preferences ->
            preferences[PW_KEY] = pW
        }
    }

    suspend fun deletePW(){
        context.dataStore.edit {preferences ->
            preferences.remove(PW_KEY)
        }
    }
}