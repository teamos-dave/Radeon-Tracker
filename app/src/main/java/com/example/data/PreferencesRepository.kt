package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.RiderPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "radeon_fuel_prefs")

class PreferencesRepository(private val context: Context) {

  private object Keys {
    val BIKE_MODEL = stringPreferencesKey("bike_model")
    val MILEAGE = doublePreferencesKey("mileage_km_per_litre")
    val FUEL_PRICE = doublePreferencesKey("fuel_price_per_litre")
    val FAVORITE_BRANCHES = stringSetPreferencesKey("favorite_branches")
  }

  val preferencesFlow: Flow<RiderPreferences> = context.dataStore.data.map { prefs ->
    RiderPreferences(
      bikeModel = prefs[Keys.BIKE_MODEL]?.takeIf { it.isNotBlank() } ?: "TVS Radeon 110cc",
      mileageKmPerLitre = prefs[Keys.MILEAGE] ?: 50.0,
      fuelPricePerLitre = prefs[Keys.FUEL_PRICE] ?: 102.28,
      favoriteBranchIds = prefs[Keys.FAVORITE_BRANCHES] ?: emptySet(),
    )
  }

  suspend fun updateSettings(
    bikeModel: String,
    mileageKmPerLitre: Double,
    fuelPricePerLitre: Double,
  ) {
    context.dataStore.edit { prefs ->
      prefs[Keys.BIKE_MODEL] = bikeModel.trim().ifEmpty { "TVS Radeon 110cc" }
      prefs[Keys.MILEAGE] = mileageKmPerLitre.coerceIn(10.0, 120.0)
      prefs[Keys.FUEL_PRICE] = fuelPricePerLitre.coerceIn(20.0, 300.0)
    }
  }

  suspend fun resetDefaults() {
    context.dataStore.edit { prefs ->
      prefs[Keys.BIKE_MODEL] = "TVS Radeon 110cc"
      prefs[Keys.MILEAGE] = 50.0
      prefs[Keys.FUEL_PRICE] = 102.28
    }
  }

  suspend fun toggleFavorite(branchId: String) {
    context.dataStore.edit { prefs ->
      val current = prefs[Keys.FAVORITE_BRANCHES] ?: emptySet()
      prefs[Keys.FAVORITE_BRANCHES] =
        if (branchId in current) current - branchId else current + branchId
    }
  }
}
