package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.BranchRepository
import com.example.model.BranchRoute
import com.example.model.RiderPreferences
import com.example.model.RouteSortOrder
import com.example.model.TripType
import com.example.ui.formatInr
import com.example.ui.formatLiters
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CardSurfaceSelected
import com.example.ui.theme.DeepBgMid
import com.example.ui.theme.DeepBgTop
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldMint
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MutedText
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RacingAmber
import com.example.ui.theme.SecondaryWhite
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationPickerSheet(
  title: String,
  routes: List<BranchRoute>,
  selectedRouteId: String?,
  favoriteIds: Set<String>,
  preferences: RiderPreferences,
  searchQuery: String,
  sortOrder: RouteSortOrder,
  onSearchChange: (String) -> Unit,
  onSortChange: (RouteSortOrder) -> Unit,
  onSelectRoute: (BranchRoute) -> Unit,
  onToggleFavorite: (String) -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = DeepBgMid,
    contentColor = PureWhite,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(horizontal = 20.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = PureWhite,
          )
          Text(
            text = "From Hamirpur • ${routes.size} locations",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText,
          )
        }
        IconButton(
          onClick = onDismiss,
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag("close_sheet_button"),
        ) {
          Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = stringResource(R.string.close_cd),
            tint = PureWhite,
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Search Input
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = {
          Text(
            text = stringResource(R.string.search_branches_hint),
            color = MutedText,
          )
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = ElectricCyan,
          )
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.close_cd),
                tint = PureWhite,
              )
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = slateTextFieldColors(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("branch_search_input"),
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Sort Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        SortChip(
          label = stringResource(R.string.sort_az),
          selected = sortOrder == RouteSortOrder.ALPHABETICAL,
          onClick = { onSortChange(RouteSortOrder.ALPHABETICAL) },
          modifier = Modifier.testTag("sort_az_chip"),
        )
        SortChip(
          label = stringResource(R.string.sort_nearest),
          selected = sortOrder == RouteSortOrder.NEAREST,
          onClick = { onSortChange(RouteSortOrder.NEAREST) },
          modifier = Modifier.testTag("sort_nearest_chip"),
        )
        SortChip(
          label = stringResource(R.string.sort_favorites),
          selected = sortOrder == RouteSortOrder.FAVORITES,
          onClick = { onSortChange(RouteSortOrder.FAVORITES) },
          modifier = Modifier.testTag("sort_favorites_chip"),
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      if (routes.isEmpty()) {
        Text(
          text = stringResource(R.string.no_matching_routes),
          style = MaterialTheme.typography.bodyLarge,
          color = SecondaryWhite,
          modifier = Modifier.padding(vertical = 32.dp),
        )
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 460.dp),
          contentPadding = PaddingValues(bottom = 24.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          items(routes, key = { it.id }) { route ->
            val single = BranchRepository.calculateEstimate(route, TripType.SINGLE, false, preferences)
            val round = BranchRepository.calculateEstimate(route, TripType.ROUND, false, preferences)
            val isSelected = route.id == selectedRouteId
            val isFav = route.id in favoriteIds

            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .clip(RoundedCornerShape(18.dp))
                .clickable { onSelectRoute(route) }
                .testTag("branch_item_${route.id}"),
              shape = RoundedCornerShape(18.dp),
              color = if (isSelected) CardSurfaceSelected else CardSurface.copy(alpha = 0.94f),
              border = BorderStroke(
                width = if (isSelected) 1.4.dp else 1.dp,
                color = if (isSelected) ElectricCyan else GlassBorder,
              ),
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(
                  imageVector = Icons.Rounded.LocationOn,
                  contentDescription = null,
                  tint = if (isSelected) ElectricCyan else RacingAmber,
                  modifier = Modifier.size(22.dp),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = route.branchName,
                    style = MaterialTheme.typography.titleMedium,
                    color = PureWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                  )
                  Text(
                    text = "Single ${route.singleTripKm} km (${formatLiters(single.fuelLiters)}) • Both ${route.roundTripKm} km",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                  )
                }

                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = formatInr(single.fuelCostInr),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = RacingAmber,
                  )
                  Text(
                    text = "Round ${formatInr(round.fuelCostInr)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedText,
                  )
                }

                IconButton(
                  onClick = { onToggleFavorite(route.id) },
                  modifier = Modifier.minimumInteractiveComponentSize(),
                ) {
                  Icon(
                    imageVector = if (isFav) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = stringResource(R.string.save_favorite),
                    tint = if (isFav) EmeraldMint else MutedText,
                    modifier = Modifier.size(20.dp),
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
  preferences: RiderPreferences,
  onSave: (bikeModel: String, mileage: Double, fuelPrice: Double) -> Unit,
  onResetDefaults: () -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var bikeModel by remember(preferences.bikeModel) { mutableStateOf(preferences.bikeModel) }
  var mileageText by remember(preferences.mileageKmPerLitre) {
    mutableStateOf(String.format(Locale.US, "%.1f", preferences.mileageKmPerLitre))
  }
  var fuelPriceText by remember(preferences.fuelPricePerLitre) {
    mutableStateOf(String.format(Locale.US, "%.2f", preferences.fuelPricePerLitre))
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = DeepBgMid,
    contentColor = PureWhite,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 6.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = stringResource(R.string.settings_title),
          style = MaterialTheme.typography.headlineMedium,
          color = PureWhite,
        )
        IconButton(
          onClick = onDismiss,
          modifier = Modifier.minimumInteractiveComponentSize(),
        ) {
          Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = stringResource(R.string.close_cd),
            tint = PureWhite,
          )
        }
      }

      OutlinedTextField(
        value = bikeModel,
        onValueChange = { bikeModel = it },
        label = { Text(stringResource(R.string.bike_model_label), color = SecondaryWhite) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = slateTextFieldColors(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("settings_bike_model_input"),
      )

      OutlinedTextField(
        value = mileageText,
        onValueChange = { mileageText = it },
        label = { Text(stringResource(R.string.mileage_label), color = SecondaryWhite) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = slateTextFieldColors(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("settings_mileage_input"),
      )

      // Mileage Quick Presets
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        listOf(45, 50, 55, 60).forEach { preset ->
          val active = mileageText.toDoubleOrNull()?.toInt() == preset
          SortChip(
            label = "$preset km/L",
            selected = active,
            onClick = { mileageText = "$preset.0" },
            modifier = Modifier.weight(1f),
          )
        }
      }

      OutlinedTextField(
        value = fuelPriceText,
        onValueChange = { fuelPriceText = it },
        label = { Text(stringResource(R.string.fuel_price_label), color = SecondaryWhite) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = slateTextFieldColors(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("settings_fuel_price_input"),
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 6.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        OutlinedButton(
          onClick = {
            onResetDefaults()
            bikeModel = "TVS Radeon 110cc"
            mileageText = "50.0"
            fuelPriceText = "102.28"
          },
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .height(52.dp)
            .testTag("reset_defaults_button"),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, GlassBorder),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
        ) {
          Text(stringResource(R.string.reset_defaults), color = PureWhite)
        }

        Button(
          onClick = {
            val mileage = mileageText.toDoubleOrNull() ?: preferences.mileageKmPerLitre
            val price = fuelPriceText.toDoubleOrNull() ?: preferences.fuelPricePerLitre
            onSave(bikeModel, mileage, price)
          },
          modifier = Modifier
            .weight(1f)
            .minimumInteractiveComponentSize()
            .height(52.dp)
            .testTag("save_settings_button"),
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = ElectricCyan,
            contentColor = DeepBgTop,
          ),
        ) {
          Text(
            text = stringResource(R.string.save_settings),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = DeepBgTop,
          )
        }
      }
    }
  }
}

@Composable
private fun SortChip(
  label: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .minimumInteractiveComponentSize()
      .clip(RoundedCornerShape(50))
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(50),
    color = if (selected) ElectricCyan else CardSurfaceElevated,
    border = BorderStroke(1.dp, if (selected) ElectricCyan else GlassBorder),
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelLarge,
      color = if (selected) DeepBgTop else PureWhite,
      maxLines = 1,
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
    )
  }
}

@Composable
private fun slateTextFieldColors() = OutlinedTextFieldDefaults.colors(
  focusedTextColor = PureWhite,
  unfocusedTextColor = PureWhite,
  cursorColor = ElectricCyan,
  focusedBorderColor = ElectricCyan,
  unfocusedBorderColor = GlassBorder,
  focusedContainerColor = CardSurface,
  unfocusedContainerColor = CardSurface.copy(alpha = 0.8f),
)
