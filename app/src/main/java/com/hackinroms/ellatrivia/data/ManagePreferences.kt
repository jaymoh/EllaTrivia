package com.hackinroms.ellatrivia.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ManagePreferences(private val context: Context) {

  companion object {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
  }

  suspend fun storeStringData(key: String, value: String) {
    val dataKey = stringPreferencesKey(key)

    context.dataStore.edit { settings ->
      settings[dataKey] = value
    }
  }

  suspend fun storeIntData(key: String, value: Int) {
    val dataKey = intPreferencesKey(key)

    context.dataStore.edit { settings ->
      settings[dataKey] = value
    }
  }

  val getStringData: (key: String) -> Flow<String> = { key ->
    context.dataStore.data
      .map { settings ->
      settings[stringPreferencesKey(key)] ?: ""
    }
  }

  val getIntData: (key: String) -> Flow<Int> = { key ->
    context.dataStore.data
      .map { preferences ->
        preferences[intPreferencesKey(key)] ?: 0
      }
  }
}

