package com.example.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.ActiveSheet
import com.example.ui.components.BrandHeader
import com.example.ui.components.DestinationPickerSheet
import com.example.ui.components.EmptyRoutePromptCard
import com.example.ui.components.RouteSelectorCard
import com.example.ui.components.SettingsSheet
import com.example.ui.components.TripDetailSection
import com.example.ui.theme.DeepBgBottom
import com.example.ui.theme.DeepBgMid
import com.example.ui.theme.DeepBgTop
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.MutedText
import com.example.ui.theme.RacingAmber

@Composable
fun RadeonAppScreen(
  viewModel: RadeonViewModel = viewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current

  BackHandler(enabled = state.activeSheet != ActiveSheet.NONE || state.selectedRoute != null) {
    if (state.activeSheet != ActiveSheet.NONE) {
      viewModel.closeSheet()
    } else if (state.selectedRoute != null) {
      viewModel.clearRoute()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(DeepBgTop, DeepBgMid, DeepBgBottom),
        ),
      )
      .drawBehind {
        // Subtle atmospheric cyan & amber ambient glow
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(ElectricCyan.copy(alpha = 0.12f), Color.Transparent),
            center = Offset(size.width * 0.88f, size.height * 0.10f),
            radius = size.width * 0.55f,
          ),
          center = Offset(size.width * 0.88f, size.height * 0.10f),
          radius = size.width * 0.55f,
        )
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(RacingAmber.copy(alpha = 0.08f), Color.Transparent),
            center = Offset(size.width * 0.12f, size.height * 0.82f),
            radius = size.width * 0.6f,
          ),
          center = Offset(size.width * 0.12f, size.height * 0.82f),
          radius = size.width * 0.6f,
        )
      },
  ) {
    Scaffold(
      containerColor = Color.Transparent,
      contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentAlignment = Alignment.TopCenter,
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp),
          verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
          BrandHeader(
            preferences = state.preferences,
            onOpenAllRoutes = { viewModel.openSheet(ActiveSheet.ALL_ROUTES) },
            onOpenSettings = { viewModel.openSheet(ActiveSheet.SETTINGS) },
          )

          RouteSelectorCard(
            sourceDisplay = state.sourceDisplay,
            destinationDisplay = state.destinationDisplay,
            selectedRoute = state.selectedRoute,
            isReversed = state.isReversed,
            quickPickRoutes = state.quickPickRoutes,
            onPickDestination = { viewModel.openSheet(ActiveSheet.DESTINATION_PICKER) },
            onSwapDirection = { viewModel.toggleDirection() },
            onQuickPick = { route -> viewModel.selectRoute(route) },
          )

          AnimatedVisibility(
            visible = state.selectedRoute == null,
            enter = fadeIn() + expandVertically(animationSpec = spring()),
            exit = fadeOut() + shrinkVertically(),
          ) {
            EmptyRoutePromptCard(
              onOpenAllRoutes = { viewModel.openSheet(ActiveSheet.ALL_ROUTES) },
            )
          }

          val single = state.singleEstimate
          val round = state.roundEstimate
          AnimatedVisibility(
            visible = state.selectedRoute != null && single != null && round != null,
            enter = fadeIn() + expandVertically(animationSpec = spring(dampingRatio = 0.8f, stiffness = 320f)),
            exit = fadeOut() + shrinkVertically(),
          ) {
            if (single != null && round != null) {
              TripDetailSection(
                singleEstimate = single,
                roundEstimate = round,
                activeTripType = state.tripType,
                preferences = state.preferences,
                isFavorite = state.selectedRoute?.id in state.preferences.favoriteBranchIds,
                onSelectTripType = { viewModel.setTripType(it) },
                onToggleFavorite = {
                  state.selectedRoute?.let { viewModel.toggleFavorite(it.id) }
                },
                onShareTrip = {
                  val summary = buildShareText(
                    bikeModel = state.preferences.bikeModel,
                    mileage = state.preferences.mileageKmPerLitre,
                    fuelPrice = state.preferences.fuelPricePerLitre,
                    single = single,
                    round = round,
                    activeType = state.tripType,
                  )
                  val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, summary)
                  }
                  context.startActivity(Intent.createChooser(shareIntent, "Share Trip Summary"))
                },
                onClearSelection = { viewModel.clearRoute() },
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "${stringResource(R.string.brand_title)} • Offline Ready",
            style = MaterialTheme.typography.labelSmall,
            color = MutedText.copy(alpha = 0.78f),
            textAlign = TextAlign.Center,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp),
          )
        }
      }
    }

    when (state.activeSheet) {
      ActiveSheet.DESTINATION_PICKER -> {
        DestinationPickerSheet(
          title = stringResource(R.string.select_destination_hint),
          routes = state.filteredRoutes,
          selectedRouteId = state.selectedRoute?.id,
          favoriteIds = state.preferences.favoriteBranchIds,
          preferences = state.preferences,
          searchQuery = state.searchQuery,
          sortOrder = state.sortOrder,
          onSearchChange = viewModel::updateSearchQuery,
          onSortChange = viewModel::updateSortOrder,
          onSelectRoute = viewModel::selectRoute,
          onToggleFavorite = viewModel::toggleFavorite,
          onDismiss = viewModel::closeSheet,
        )
      }

      ActiveSheet.ALL_ROUTES -> {
        DestinationPickerSheet(
          title = stringResource(R.string.all_routes_title),
          routes = state.filteredRoutes,
          selectedRouteId = state.selectedRoute?.id,
          favoriteIds = state.preferences.favoriteBranchIds,
          preferences = state.preferences,
          searchQuery = state.searchQuery,
          sortOrder = state.sortOrder,
          onSearchChange = viewModel::updateSearchQuery,
          onSortChange = viewModel::updateSortOrder,
          onSelectRoute = viewModel::selectRoute,
          onToggleFavorite = viewModel::toggleFavorite,
          onDismiss = viewModel::closeSheet,
        )
      }

      ActiveSheet.SETTINGS -> {
        SettingsSheet(
          preferences = state.preferences,
          onSave = viewModel::updateSettings,
          onResetDefaults = viewModel::resetDefaults,
          onDismiss = viewModel::closeSheet,
        )
      }

      ActiveSheet.NONE -> Unit
    }
  }
}
