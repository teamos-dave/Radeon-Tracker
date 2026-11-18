package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BranchRepository
import com.example.data.PreferencesRepository
import com.example.model.ActiveSheet
import com.example.model.BranchRoute
import com.example.model.RiderPreferences
import com.example.model.RouteSortOrder
import com.example.model.TripEstimate
import com.example.model.TripType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RadeonUiState(
  val preferences: RiderPreferences = RiderPreferences(),
  val allRoutes: List<BranchRoute> = BranchRepository.allRoutes,
  val filteredRoutes: List<BranchRoute> = BranchRepository.allRoutes,
  val quickPickRoutes: List<BranchRoute> = emptyList(),
  val selectedRoute: BranchRoute? = null,
  val tripType: TripType = TripType.SINGLE,
  val isReversed: Boolean = false,
  val singleEstimate: TripEstimate? = null,
  val roundEstimate: TripEstimate? = null,
  val searchQuery: String = "",
  val sortOrder: RouteSortOrder = RouteSortOrder.ALPHABETICAL,
  val activeSheet: ActiveSheet = ActiveSheet.NONE,
) {
  val activeEstimate: TripEstimate?
    get() = when (tripType) {
      TripType.SINGLE -> singleEstimate
      TripType.ROUND -> roundEstimate
    }

  val sourceDisplay: String
    get() = when {
      selectedRoute == null -> "Hamirpur"
      isReversed -> selectedRoute.to
      else -> selectedRoute.from
    }

  val destinationDisplay: String?
    get() = when {
      selectedRoute == null -> null
      isReversed -> selectedRoute.from
      else -> selectedRoute.to
    }
}

private data class LocalUiState(
  val selectedRoute: BranchRoute? = null,
  val tripType: TripType = TripType.SINGLE,
  val isReversed: Boolean = false,
  val searchQuery: String = "",
  val sortOrder: RouteSortOrder = RouteSortOrder.ALPHABETICAL,
  val activeSheet: ActiveSheet = ActiveSheet.NONE,
)

class RadeonViewModel(application: Application) : AndroidViewModel(application) {

  private val prefsRepo = PreferencesRepository(application.applicationContext)
  private val localState = MutableStateFlow(LocalUiState())

  private val quickPicks = BranchRepository.quickPickIds.mapNotNull { id ->
    BranchRepository.allRoutes.find { it.id == id }
  }

  val uiState: StateFlow<RadeonUiState> = combine(
    prefsRepo.preferencesFlow,
    localState,
  ) { prefs, local ->
    val query = local.searchQuery.trim()
    val searched = if (query.isEmpty()) {
      BranchRepository.allRoutes
    } else {
      BranchRepository.allRoutes.filter {
        it.branchName.contains(query, ignoreCase = true) ||
          it.terrainNote.contains(query, ignoreCase = true)
      }
    }

    val sorted = when (local.sortOrder) {
      RouteSortOrder.ALPHABETICAL -> searched.sortedBy { it.branchName }
      RouteSortOrder.NEAREST -> searched.sortedBy { it.singleTripKm }
      RouteSortOrder.FAVORITES -> {
        val favs = searched.filter { it.id in prefs.favoriteBranchIds }
        favs.ifEmpty { searched.sortedBy { it.branchName } }
      }
    }

    val route = local.selectedRoute
    val singleEst = route?.let {
      BranchRepository.calculateEstimate(it, TripType.SINGLE, local.isReversed, prefs)
    }
    val roundEst = route?.let {
      BranchRepository.calculateEstimate(it, TripType.ROUND, local.isReversed, prefs)
    }

    RadeonUiState(
      preferences = prefs,
      allRoutes = BranchRepository.allRoutes,
      filteredRoutes = sorted,
      quickPickRoutes = quickPicks,
      selectedRoute = route,
      tripType = local.tripType,
      isReversed = local.isReversed,
      singleEstimate = singleEst,
      roundEstimate = roundEst,
      searchQuery = local.searchQuery,
      sortOrder = local.sortOrder,
      activeSheet = local.activeSheet,
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = RadeonUiState(quickPickRoutes = quickPicks),
  )

  fun selectRoute(route: BranchRoute) {
    localState.update {
      it.copy(
        selectedRoute = route,
        activeSheet = ActiveSheet.NONE,
        searchQuery = "",
      )
    }
  }

  fun clearRoute() {
    localState.update {
      it.copy(
        selectedRoute = null,
        isReversed = false,
      )
    }
  }

  fun setTripType(type: TripType) {
    localState.update { it.copy(tripType = type) }
  }

  fun toggleDirection() {
    localState.update {
      if (it.selectedRoute != null) it.copy(isReversed = !it.isReversed) else it
    }
  }

  fun openSheet(sheet: ActiveSheet) {
    localState.update { it.copy(activeSheet = sheet) }
  }

  fun closeSheet() {
    localState.update { it.copy(activeSheet = ActiveSheet.NONE, searchQuery = "") }
  }

  fun updateSearchQuery(query: String) {
    localState.update { it.copy(searchQuery = query) }
  }

  fun updateSortOrder(order: RouteSortOrder) {
    localState.update { it.copy(sortOrder = order) }
  }

  fun toggleFavorite(branchId: String) {
    viewModelScope.launch {
      prefsRepo.toggleFavorite(branchId)
    }
  }

  fun updateSettings(
    bikeModel: String,
    mileageKmPerLitre: Double,
    fuelPricePerLitre: Double,
  ) {
    viewModelScope.launch {
      prefsRepo.updateSettings(bikeModel, mileageKmPerLitre, fuelPricePerLitre)
      closeSheet()
    }
  }

  fun resetDefaults() {
    viewModelScope.launch {
      prefsRepo.resetDefaults()
    }
  }
}
