package com.example.data

import com.example.model.BranchRoute
import com.example.model.RiderPreferences
import com.example.model.TripEstimate
import com.example.model.TripType
import kotlin.math.roundToInt

object BranchRepository {

  val allRoutes: List<BranchRoute> = listOf(
    BranchRoute(id = "barsar", branchName = "Barsar", to = "Barsar", singleTripKm = 40, roundTripKm = 80, terrainNote = "District Highway"),
    BranchRoute(id = "una", branchName = "Una", to = "Una", singleTripKm = 75, roundTripKm = 150, terrainNote = "Foothill Corridor"),
    BranchRoute(id = "amb", branchName = "Amb", to = "Amb", singleTripKm = 70, roundTripKm = 140, terrainNote = "Valley Route"),
    BranchRoute(id = "kangra", branchName = "Kangra", to = "Kangra", singleTripKm = 80, roundTripKm = 160, terrainNote = "NH-88 Scenic"),
    BranchRoute(id = "nurpur", branchName = "Nurpur", to = "Nurpur", singleTripKm = 130, roundTripKm = 260, terrainNote = "Kangra Border"),
    BranchRoute(id = "chamba", branchName = "Chamba", to = "Chamba", singleTripKm = 215, roundTripKm = 430, terrainNote = "High Mountain"),
    BranchRoute(id = "jawali", branchName = "Jawali", to = "Jawali", singleTripKm = 110, roundTripKm = 220, terrainNote = "Pong Dam Belt"),
    BranchRoute(id = "dharamshala", branchName = "Dharamshala", to = "Dharamshala", singleTripKm = 94, roundTripKm = 188, terrainNote = "Dhauladhar Climb"),
    BranchRoute(id = "palampur", branchName = "Palampur", to = "Palampur", singleTripKm = 72, roundTripKm = 144, terrainNote = "Tea Garden Road"),
    BranchRoute(id = "baijnath", branchName = "Baijnath", to = "Baijnath", singleTripKm = 90, roundTripKm = 180, terrainNote = "Hill Valley"),
    BranchRoute(id = "joginder_nagar", branchName = "Joginder Nagar", to = "Joginder Nagar", singleTripKm = 110, roundTripKm = 220, terrainNote = "Mandi-Kangra Link"),
    BranchRoute(id = "jaisinghpur", branchName = "Jaisinghpur", to = "Jaisinghpur", singleTripKm = 51, roundTripKm = 102, terrainNote = "Beas Belt"),
    BranchRoute(id = "nadaun", branchName = "Nadaun", to = "Nadaun", singleTripKm = 35, roundTripKm = 70, terrainNote = "Short Local Run"),
    BranchRoute(id = "bilaspur", branchName = "Bilaspur", to = "Bilaspur", singleTripKm = 68, roundTripKm = 136, terrainNote = "Gobind Sagar Route"),
    BranchRoute(id = "sarkaghat", branchName = "Sarkaghat", to = "Sarkaghat", singleTripKm = 41, roundTripKm = 82, terrainNote = "Mandi Border"),
    BranchRoute(id = "sunder_nagar", branchName = "Sunder Nagar", to = "Sunder Nagar", singleTripKm = 80, roundTripKm = 160, terrainNote = "NH-154 Link"),
    BranchRoute(id = "mandi", branchName = "Mandi", to = "Mandi", singleTripKm = 105, roundTripKm = 210, terrainNote = "Winding River Road"),
    BranchRoute(id = "kullu", branchName = "Kullu", to = "Kullu", singleTripKm = 170, roundTripKm = 340, terrainNote = "Mountain Valley"),
    BranchRoute(id = "manali", branchName = "Manali", to = "Manali", singleTripKm = 210, roundTripKm = 420, terrainNote = "Alpine Highway"),
    BranchRoute(id = "dehra", branchName = "Dehra", to = "Dehra", singleTripKm = 56, roundTripKm = 112, terrainNote = "Beas Bridge Road"),
  )

  val quickPickIds: List<String> = listOf("nadaun", "barsar", "una", "kangra", "dharamshala", "mandi", "manali")

  fun calculateEstimate(
    route: BranchRoute,
    tripType: TripType,
    isReversed: Boolean,
    preferences: RiderPreferences,
  ): TripEstimate {
    val safeMileage = preferences.mileageKmPerLitre.coerceAtLeast(1.0)
    val safePrice = preferences.fuelPricePerLitre.coerceAtLeast(0.0)
    val distanceKm = when (tripType) {
      TripType.SINGLE -> route.singleTripKm
      TripType.ROUND -> route.roundTripKm
    }
    val fuelLiters = distanceKm / safeMileage
    val fuelCostInr = fuelLiters * safePrice
    val costPerKmInr = safePrice / safeMileage
    // Realistic average speed for Himachal Pradesh roads (~38 km/h on a 110cc commuter)
    val estimatedMinutes = ((distanceKm / 38.0) * 60.0).roundToInt().coerceAtLeast(15)

    val source = if (isReversed) route.to else route.from
    val destination = if (isReversed) route.from else route.to

    return TripEstimate(
      route = route,
      tripType = tripType,
      sourceName = source,
      destinationName = destination,
      distanceKm = distanceKm,
      fuelLiters = fuelLiters,
      fuelCostInr = fuelCostInr,
      costPerKmInr = costPerKmInr,
      estimatedMinutes = estimatedMinutes,
    )
  }
}
