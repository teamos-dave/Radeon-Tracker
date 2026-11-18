package com.example.model

enum class TripType {
  SINGLE,
  ROUND,
}

enum class RouteSortOrder {
  ALPHABETICAL,
  NEAREST,
  FAVORITES,
}

enum class ActiveSheet {
  NONE,
  DESTINATION_PICKER,
  ALL_ROUTES,
  SETTINGS,
}

data class BranchRoute(
  val id: String,
  val branchName: String,
  val from: String = "Hamirpur",
  val to: String,
  val singleTripKm: Int,
  val roundTripKm: Int,
  val terrainNote: String,
)

data class TripEstimate(
  val route: BranchRoute,
  val tripType: TripType,
  val sourceName: String,
  val destinationName: String,
  val distanceKm: Int,
  val fuelLiters: Double,
  val fuelCostInr: Double,
  val costPerKmInr: Double,
  val estimatedMinutes: Int,
)

data class RiderPreferences(
  val bikeModel: String = "TVS Radeon 110cc",
  val mileageKmPerLitre: Double = 50.0,
  val fuelPricePerLitre: Double = 102.28,
  val favoriteBranchIds: Set<String> = emptySet(),
)
