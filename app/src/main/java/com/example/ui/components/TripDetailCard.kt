package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.RiderPreferences
import com.example.model.TripEstimate
import com.example.model.TripType
import com.example.ui.formatDuration
import com.example.ui.formatInr
import com.example.ui.formatLiters
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CardSurfaceSelected
import com.example.ui.theme.DeepBgTop
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldMint
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MutedText
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RacingAmber
import com.example.ui.theme.SecondaryWhite
import java.util.Locale

@Composable
fun EmptyRoutePromptCard(
  onOpenAllRoutes: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(26.dp),
    color = CardSurface.copy(alpha = 0.78f),
    border = BorderStroke(1.dp, GlassBorder),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 28.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(ElectricCyan.copy(alpha = 0.25f), CardSurfaceElevated.copy(alpha = 0.6f)),
            ),
          )
          .border(1.dp, ElectricCyan.copy(alpha = 0.45f), CircleShape),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.TwoWheeler,
          contentDescription = null,
          tint = ElectricCyan,
          modifier = Modifier.size(32.dp),
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = stringResource(R.string.empty_prompt_title),
        style = MaterialTheme.typography.titleLarge,
        color = PureWhite,
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = stringResource(R.string.empty_prompt_body),
        style = MaterialTheme.typography.bodyMedium,
        color = SecondaryWhite.copy(alpha = 0.85f),
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(18.dp))

      OutlinedButton(
        onClick = onOpenAllRoutes,
        modifier = Modifier
          .minimumInteractiveComponentSize()
          .testTag("empty_view_all_routes_button"),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.6f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
      ) {
        Icon(
          imageVector = Icons.Rounded.FormatListBulleted,
          contentDescription = null,
          tint = ElectricCyan,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = stringResource(R.string.view_all_routes),
          style = MaterialTheme.typography.labelLarge,
          color = PureWhite,
        )
      }
    }
  }
}

@Composable
fun TripDetailSection(
  singleEstimate: TripEstimate,
  roundEstimate: TripEstimate,
  activeTripType: TripType,
  preferences: RiderPreferences,
  isFavorite: Boolean,
  onSelectTripType: (TripType) -> Unit,
  onToggleFavorite: () -> Unit,
  onShareTrip: () -> Unit,
  onClearSelection: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val active = if (activeTripType == TripType.SINGLE) singleEstimate else roundEstimate

  val animatedKm by animateIntAsState(
    targetValue = active.distanceKm,
    animationSpec = spring(dampingRatio = 0.85f, stiffness = 320f),
    label = "animatedKm",
  )
  val animatedFuel by animateFloatAsState(
    targetValue = active.fuelLiters.toFloat(),
    animationSpec = spring(dampingRatio = 0.85f, stiffness = 320f),
    label = "animatedFuel",
  )
  val animatedCost by animateFloatAsState(
    targetValue = active.fuelCostInr.toFloat(),
    animationSpec = spring(dampingRatio = 0.85f, stiffness = 320f),
    label = "animatedCost",
  )

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    // Segmented Mode Toggle: Single Trip vs Round Trip
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(50),
      color = CardSurface.copy(alpha = 0.94f),
      border = BorderStroke(1.dp, GlassBorder),
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        TripModePill(
          title = stringResource(R.string.single_trip),
          subtitle = "${singleEstimate.distanceKm} km",
          icon = Icons.AutoMirrored.Rounded.ArrowForward,
          selected = activeTripType == TripType.SINGLE,
          accentColor = ElectricCyan,
          onClick = { onSelectTripType(TripType.SINGLE) },
          modifier = Modifier
            .weight(1f)
            .testTag("single_trip_tab"),
        )
        TripModePill(
          title = stringResource(R.string.round_trip),
          subtitle = "${roundEstimate.distanceKm} km",
          icon = Icons.Rounded.CompareArrows,
          selected = activeTripType == TripType.ROUND,
          accentColor = RacingAmber,
          onClick = { onSelectTripType(TripType.ROUND) },
          modifier = Modifier
            .weight(1f)
            .testTag("round_trip_tab"),
        )
      }
    }

    // Hero Trip Summary Card
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(28.dp),
      color = Color.Transparent,
      border = BorderStroke(1.2.dp, GlassBorder),
      shadowElevation = 12.dp,
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.linearGradient(
              colors = listOf(
                CardSurfaceSelected.copy(alpha = 0.96f),
                CardSurface.copy(alpha = 0.98f),
              ),
            ),
          )
          .padding(20.dp),
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (activeTripType == TripType.SINGLE) {
                  "${stringResource(R.string.single_trip)} • ${stringResource(R.string.one_way_sub)}"
                } else {
                  "${stringResource(R.string.round_trip)} • ${stringResource(R.string.both_side_sub)}"
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (activeTripType == TripType.SINGLE) ElectricCyan else RacingAmber,
              )
              Spacer(modifier = Modifier.height(4.dp))
              AnimatedContent(
                targetState = "${active.sourceName} ${if (activeTripType == TripType.SINGLE) "→" else "↔"} ${active.destinationName}",
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "routeHeader",
              ) { routeText ->
                Text(
                  text = routeText,
                  style = MaterialTheme.typography.headlineMedium,
                  color = PureWhite,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }
            }

            Surface(
              color = CardSurfaceElevated.copy(alpha = 0.95f),
              shape = RoundedCornerShape(50),
              border = BorderStroke(1.dp, GlassBorder),
            ) {
              Text(
                text = "${formatInr(active.costPerKmInr)}/km",
                style = MaterialTheme.typography.labelMedium,
                color = PureWhite,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          Text(
            text = stringResource(R.string.estimated_cost_label),
            style = MaterialTheme.typography.labelMedium,
            color = SecondaryWhite,
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = formatInr(animatedCost.toDouble()),
            style = MaterialTheme.typography.displayLarge,
            color = RacingAmber,
            modifier = Modifier.testTag("hero_cost_text"),
          )
          Text(
            text = "At ${formatInr(preferences.fuelPricePerLitre)}/L • ${
              String.format(Locale.US, "%.0f", preferences.mileageKmPerLitre)
            } km/L mileage",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
          )

          Spacer(modifier = Modifier.height(18.dp))

          // 3 Key Metric Tiles
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            MetricTile(
              icon = Icons.Rounded.Route,
              label = stringResource(R.string.distance_label),
              value = "$animatedKm km",
              accent = ElectricCyan,
              modifier = Modifier.weight(1f),
            )
            MetricTile(
              icon = Icons.Rounded.LocalGasStation,
              label = stringResource(R.string.fuel_needed_label),
              value = formatLiters(animatedFuel.toDouble()),
              accent = RacingAmber,
              modifier = Modifier.weight(1f),
            )
            MetricTile(
              icon = Icons.Rounded.Schedule,
              label = stringResource(R.string.ride_time_label),
              value = "~${formatDuration(active.estimatedMinutes)}",
              accent = EmeraldMint,
              modifier = Modifier.weight(1f),
            )
          }
        }
      }
    }

    // Side-by-Side Comparison Card
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      color = CardSurface.copy(alpha = 0.92f),
      border = BorderStroke(1.dp, GlassBorder),
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
      ) {
        Text(
          text = stringResource(R.string.trip_comparison_title),
          style = MaterialTheme.typography.labelSmall,
          color = MutedText,
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          ComparisonColumn(
            title = stringResource(R.string.single_trip),
            subtitle = stringResource(R.string.one_way_sub),
            estimate = singleEstimate,
            isActive = activeTripType == TripType.SINGLE,
            accentColor = ElectricCyan,
            onClick = { onSelectTripType(TripType.SINGLE) },
            modifier = Modifier
              .weight(1f)
              .testTag("compare_single_card"),
          )
          ComparisonColumn(
            title = stringResource(R.string.round_trip),
            subtitle = stringResource(R.string.both_side_sub),
            estimate = roundEstimate,
            isActive = activeTripType == TripType.ROUND,
            accentColor = RacingAmber,
            onClick = { onSelectTripType(TripType.ROUND) },
            modifier = Modifier
              .weight(1f)
              .testTag("compare_round_card"),
          )
        }
      }
    }

    // Action Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Button(
        onClick = onShareTrip,
        modifier = Modifier
          .weight(1f)
          .minimumInteractiveComponentSize()
          .height(50.dp)
          .testTag("share_trip_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = ElectricCyan,
          contentColor = DeepBgTop,
        ),
      ) {
        Icon(
          imageVector = Icons.Rounded.Share,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = stringResource(R.string.share_trip),
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = DeepBgTop,
        )
      }

      OutlinedButton(
        onClick = onToggleFavorite,
        modifier = Modifier
          .minimumInteractiveComponentSize()
          .height(50.dp)
          .testTag("favorite_route_button"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (isFavorite) EmeraldMint else GlassBorder),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = if (isFavorite) EmeraldMint.copy(alpha = 0.14f) else CardSurfaceElevated.copy(alpha = 0.7f),
          contentColor = PureWhite,
        ),
      ) {
        Icon(
          imageVector = if (isFavorite) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
          contentDescription = null,
          tint = if (isFavorite) EmeraldMint else PureWhite,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = stringResource(if (isFavorite) R.string.saved_favorite else R.string.save_favorite),
          style = MaterialTheme.typography.labelLarge,
          color = PureWhite,
        )
      }

      OutlinedButton(
        onClick = onClearSelection,
        modifier = Modifier
          .minimumInteractiveComponentSize()
          .height(50.dp)
          .testTag("clear_route_button"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GlassBorder),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = CardSurfaceElevated.copy(alpha = 0.65f),
          contentColor = PureWhite,
        ),
      ) {
        Icon(
          imageVector = Icons.Rounded.Refresh,
          contentDescription = stringResource(R.string.clear_selection),
          tint = PureWhite,
          modifier = Modifier.size(18.dp),
        )
      }
    }
  }
}

@Composable
private fun TripModePill(
  title: String,
  subtitle: String,
  icon: ImageVector,
  selected: Boolean,
  accentColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .minimumInteractiveComponentSize()
      .clip(RoundedCornerShape(50))
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(50),
    color = if (selected) CardSurfaceSelected else Color.Transparent,
    border = if (selected) BorderStroke(1.dp, accentColor.copy(alpha = 0.75f)) else null,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (selected) accentColor else MutedText,
        modifier = Modifier.size(18.dp),
      )
      Spacer(modifier = Modifier.width(6.dp))
      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.labelLarge,
          color = if (selected) PureWhite else SecondaryWhite,
          maxLines = 1,
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.labelSmall,
          color = if (selected) accentColor else MutedText,
          maxLines = 1,
        )
      }
    }
  }
}

@Composable
private fun MetricTile(
  icon: ImageVector,
  label: String,
  value: String,
  accent: Color,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(18.dp),
    color = CardSurfaceElevated.copy(alpha = 0.78f),
    border = BorderStroke(1.dp, GlassBorder),
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.Start,
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = accent,
        modifier = Modifier.size(18.dp),
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = PureWhite,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = MutedText,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
private fun ComparisonColumn(
  title: String,
  subtitle: String,
  estimate: TripEstimate,
  isActive: Boolean,
  accentColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .minimumInteractiveComponentSize()
      .clip(RoundedCornerShape(18.dp))
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(18.dp),
    color = if (isActive) CardSurfaceSelected.copy(alpha = 0.85f) else CardSurfaceElevated.copy(alpha = 0.65f),
    border = BorderStroke(
      width = if (isActive) 1.4.dp else 1.dp,
      color = if (isActive) accentColor.copy(alpha = 0.8f) else GlassBorder,
    ),
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelLarge,
          color = PureWhite,
        )
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (isActive) accentColor else MutedText.copy(alpha = 0.4f)),
        )
      }
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = MutedText,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "${estimate.distanceKm} km",
        style = MaterialTheme.typography.titleLarge,
        color = PureWhite,
      )
      Text(
        text = formatLiters(estimate.fuelLiters),
        style = MaterialTheme.typography.bodyMedium,
        color = SecondaryWhite,
      )
      Text(
        text = formatInr(estimate.fuelCostInr),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = accentColor,
      )
    }
  }
}
