package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.RiderPreferences
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RacingAmber
import com.example.ui.theme.SecondaryWhite
import java.util.Locale

@Composable
fun BrandHeader(
  preferences: RiderPreferences,
  onOpenAllRoutes: () -> Unit,
  onOpenSettings: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f),
    ) {
      // Custom Radeon Tracker Emblem with Cyan-Amber Ring
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              colors = listOf(ElectricCyan, RacingAmber),
            ),
          )
          .padding(2.dp),
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_radeon_logo),
          contentDescription = stringResource(R.string.brand_logo_cd),
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(50.dp)
            .clip(CircleShape),
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = stringResource(R.string.brand_title),
          style = MaterialTheme.typography.titleLarge,
          color = PureWhite,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.size(3.dp))
        Surface(
          color = CardSurfaceElevated.copy(alpha = 0.85f),
          shape = RoundedCornerShape(50),
          border = BorderStroke(1.dp, GlassBorder),
        ) {
          Text(
            text = "${preferences.bikeModel} • ${
              String.format(Locale.US, "%.0f", preferences.mileageKmPerLitre)
            } km/L",
            style = MaterialTheme.typography.labelMedium,
            color = SecondaryWhite,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      GlassHeaderIconButton(
        onClick = onOpenAllRoutes,
        testTag = "all_routes_button",
      ) {
        Icon(
          imageVector = Icons.Rounded.FormatListBulleted,
          contentDescription = stringResource(R.string.open_all_routes_cd),
          tint = ElectricCyan,
          modifier = Modifier.size(22.dp),
        )
      }
      GlassHeaderIconButton(
        onClick = onOpenSettings,
        testTag = "settings_button",
      ) {
        Icon(
          imageVector = Icons.Rounded.Tune,
          contentDescription = stringResource(R.string.open_settings_cd),
          tint = RacingAmber,
          modifier = Modifier.size(22.dp),
        )
      }
    }
  }
}

@Composable
private fun GlassHeaderIconButton(
  onClick: () -> Unit,
  testTag: String,
  content: @Composable () -> Unit,
) {
  IconButton(
    onClick = onClick,
    modifier = Modifier
      .minimumInteractiveComponentSize()
      .size(48.dp)
      .clip(CircleShape)
      .background(CardSurfaceElevated.copy(alpha = 0.85f))
      .border(1.dp, GlassBorder, CircleShape)
      .testTag(testTag),
  ) {
    content()
  }
}
