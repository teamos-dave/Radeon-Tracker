package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.BranchRoute
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

@Composable
fun RouteSelectorCard(
  sourceDisplay: String,
  destinationDisplay: String?,
  selectedRoute: BranchRoute?,
  isReversed: Boolean,
  quickPickRoutes: List<BranchRoute>,
  onPickDestination: () -> Unit,
  onSwapDirection: () -> Unit,
  onQuickPick: (BranchRoute) -> Unit,
  modifier: Modifier = Modifier,
) {
  val swapRotation by animateFloatAsState(
    targetValue = if (isReversed) 180f else 0f,
    animationSpec = spring(dampingRatio = 0.7f, stiffness = 360f),
    label = "swapRotation",
  )

  Column(modifier = modifier.fillMaxWidth()) {
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(26.dp),
      color = CardSurface.copy(alpha = 0.94f),
      border = BorderStroke(1.dp, GlassBorder),
      shadowElevation = 10.dp,
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp),
      ) {
        // FROM Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(ElectricCyan.copy(alpha = 0.14f))
              .border(1.dp, ElectricCyan.copy(alpha = 0.48f), CircleShape),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Rounded.MyLocation,
              contentDescription = null,
              tint = ElectricCyan,
              modifier = Modifier.size(18.dp),
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = stringResource(R.string.source_label),
              style = MaterialTheme.typography.labelSmall,
              color = MutedText,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = sourceDisplay,
                style = MaterialTheme.typography.titleLarge,
                color = PureWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
              if (sourceDisplay == "Hamirpur") {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                  color = EmeraldMint.copy(alpha = 0.14f),
                  shape = RoundedCornerShape(50),
                ) {
                  Text(
                    text = "HOME",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldMint,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                  )
                }
              }
            }
          }
        }

        // Connector Divider + Swap Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.CenterEnd,
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier = Modifier.width(38.dp),
              contentAlignment = Alignment.Center,
            ) {
              Box(
                modifier = Modifier
                  .width(2.dp)
                  .height(24.dp)
                  .background(
                    Brush.verticalGradient(
                      colors = listOf(ElectricCyan.copy(alpha = 0.7f), RacingAmber.copy(alpha = 0.85f)),
                    ),
                  ),
              )
            }
            HorizontalDivider(
              modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
              color = GlassBorder,
            )
            IconButton(
              onClick = onSwapDirection,
              enabled = selectedRoute != null,
              modifier = Modifier
                .minimumInteractiveComponentSize()
                .size(48.dp)
                .clip(CircleShape)
                .background(
                  if (selectedRoute != null) CardSurfaceElevated else CardSurfaceElevated.copy(alpha = 0.45f),
                )
                .border(1.dp, GlassBorder, CircleShape)
                .testTag("swap_direction_button"),
            ) {
              Icon(
                imageVector = Icons.Rounded.SwapVert,
                contentDescription = stringResource(R.string.swap_direction_cd),
                tint = if (selectedRoute != null) RacingAmber else MutedText.copy(alpha = 0.45f),
                modifier = Modifier
                  .size(22.dp)
                  .graphicsLayer { rotationZ = swapRotation },
              )
            }
          }
        }

        // TO (Destination) Interactive Selector Box
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onPickDestination)
            .testTag("destination_selector"),
          shape = RoundedCornerShape(18.dp),
          color = if (destinationDisplay != null) {
            CardSurfaceSelected.copy(alpha = 0.85f)
          } else {
            CardSurfaceElevated.copy(alpha = 0.95f)
          },
          border = BorderStroke(
            width = 1.2.dp,
            color = if (destinationDisplay != null) ElectricCyan.copy(alpha = 0.7f) else GlassBorder,
          ),
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(RacingAmber.copy(alpha = 0.14f))
                .border(1.dp, RacingAmber.copy(alpha = 0.5f), CircleShape),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = if (destinationDisplay != null) Icons.Rounded.LocationOn else Icons.Rounded.Search,
                contentDescription = null,
                tint = RacingAmber,
                modifier = Modifier.size(20.dp),
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = stringResource(R.string.destination_label),
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = destinationDisplay ?: stringResource(R.string.select_destination_hint),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PureWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
              if (destinationDisplay == null) {
                Text(
                  text = stringResource(R.string.select_destination_sub),
                  style = MaterialTheme.typography.bodyMedium,
                  color = SecondaryWhite.copy(alpha = 0.8f),
                )
              } else if (selectedRoute != null) {
                Text(
                  text = "${selectedRoute.terrainNote} • ${selectedRoute.singleTripKm} km one-way",
                  style = MaterialTheme.typography.bodyMedium,
                  color = SecondaryWhite,
                )
              }
            }

            Icon(
              imageVector = Icons.Rounded.ExpandMore,
              contentDescription = null,
              tint = PureWhite,
              modifier = Modifier.size(24.dp),
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Quick Pick Horizontal Chips
    Text(
      text = stringResource(R.string.popular_routes_label),
      style = MaterialTheme.typography.labelSmall,
      color = MutedText,
      modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      quickPickRoutes.forEach { route ->
        val isSelected = selectedRoute?.id == route.id
        Surface(
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(50))
            .clickable { onQuickPick(route) }
            .testTag("quick_pick_${route.id}"),
          shape = RoundedCornerShape(50),
          color = if (isSelected) ElectricCyan else CardSurfaceElevated.copy(alpha = 0.82f),
          border = BorderStroke(
            1.dp,
            if (isSelected) ElectricCyan else GlassBorder,
          ),
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = route.branchName,
              style = MaterialTheme.typography.labelLarge,
              color = if (isSelected) DeepBgTop else PureWhite,
              fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${route.singleTripKm} km",
              style = MaterialTheme.typography.labelMedium,
              color = if (isSelected) DeepBgTop.copy(alpha = 0.78f) else MutedText,
            )
          }
        }
      }
    }
  }
}
