package com.example

import com.example.data.BranchRepository
import com.example.model.RiderPreferences
import com.example.model.TripType
import org.junit.Assert.assertEquals
import org.junit.Test

class BranchRepositoryTest {

  @Test
  fun allTwentyRoutesMatchSuppliedSpreadsheet() {
    assertEquals(20, BranchRepository.allRoutes.size)
    BranchRepository.allRoutes.forEach { route ->
      assertEquals("Hamirpur", route.from)
      assertEquals(route.singleTripKm * 2, route.roundTripKm)
    }
  }

  @Test
  fun singleAndRoundTripFuelCalculationsAreAccurate() {
    val prefs = RiderPreferences() // 50 km/L, ₹102.28/L
    val barsar = BranchRepository.allRoutes.first { it.id == "barsar" }

    val single = BranchRepository.calculateEstimate(barsar, TripType.SINGLE, isReversed = false, prefs)
    assertEquals(40, single.distanceKm)
    assertEquals(0.80, single.fuelLiters, 0.001)
    assertEquals(81.824, single.fuelCostInr, 0.001)
    assertEquals("Hamirpur", single.sourceName)
    assertEquals("Barsar", single.destinationName)

    val roundReversed = BranchRepository.calculateEstimate(barsar, TripType.ROUND, isReversed = true, prefs)
    assertEquals(80, roundReversed.distanceKm)
    assertEquals(1.60, roundReversed.fuelLiters, 0.001)
    assertEquals(163.648, roundReversed.fuelCostInr, 0.001)
    assertEquals("Barsar", roundReversed.sourceName)
    assertEquals("Hamirpur", roundReversed.destinationName)
  }

  @Test
  fun longestRouteChambaCalculatesCorrectly() {
    val prefs = RiderPreferences()
    val chamba = BranchRepository.allRoutes.first { it.id == "chamba" }
    val round = BranchRepository.calculateEstimate(chamba, TripType.ROUND, isReversed = false, prefs)
    assertEquals(430, round.distanceKm)
    assertEquals(8.60, round.fuelLiters, 0.001)
    assertEquals(879.608, round.fuelCostInr, 0.001)
  }
}
