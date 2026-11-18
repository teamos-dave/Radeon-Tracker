package com.example.ui

import com.example.model.TripEstimate
import com.example.model.TripType
import java.util.Locale

fun formatInr(amount: Double): String {
  return String.format(Locale.US, "₹%.2f", amount)
}

fun formatLiters(liters: Double): String {
  return String.format(Locale.US, "%.2f L", liters)
}

fun formatDuration(minutes: Int): String {
  val hrs = minutes / 60
  val mins = minutes % 60
  return when {
    hrs == 0 -> "${mins}m"
    mins == 0 -> "${hrs}h"
    else -> "${hrs}h ${mins}m"
  }
}

fun buildShareText(
  bikeModel: String,
  mileage: Double,
  fuelPrice: Double,
  single: TripEstimate,
  round: TripEstimate,
  activeType: TripType,
): String {
  val modeLabel = if (activeType == TripType.SINGLE) "Single Trip (One Way)" else "Round Trip (Both Side)"
  val active = if (activeType == TripType.SINGLE) single else round
  return buildString {
    appendLine("🏍️ Radeon Tracker — Trip Summary")
    appendLine("Bike: $bikeModel (${String.format(Locale.US, "%.0f", mileage)} km/L)")
    appendLine("Petrol Rate: ${formatInr(fuelPrice)}/L")
    appendLine("Route: ${active.sourceName} → ${active.destinationName}")
    appendLine("Selected Mode: $modeLabel")
    appendLine("────────────────────")
    appendLine("• Single Trip: ${single.distanceKm} km | ${formatLiters(single.fuelLiters)} | ${formatInr(single.fuelCostInr)}")
    appendLine("• Round Trip: ${round.distanceKm} km | ${formatLiters(round.fuelLiters)} | ${formatInr(round.fuelCostInr)}")
    appendLine("• Est. Ride Time (${modeLabel}): ~${formatDuration(active.estimatedMinutes)}")
  }.trim()
}
