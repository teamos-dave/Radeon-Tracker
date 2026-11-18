package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val ObsidianTrackerColorScheme = darkColorScheme(
  primary = RacingAmber,
  onPrimary = DeepBgTop,
  primaryContainer = CardSurfaceSelected,
  onPrimaryContainer = PureWhite,
  secondary = ElectricCyan,
  onSecondary = DeepBgTop,
  secondaryContainer = CardSurfaceElevated,
  onSecondaryContainer = PureWhite,
  tertiary = EmeraldMint,
  onTertiary = DeepBgTop,
  background = DeepBgTop,
  onBackground = PureWhite,
  surface = CardSurface,
  onSurface = PureWhite,
  surfaceVariant = CardSurfaceElevated,
  onSurfaceVariant = SecondaryWhite,
  outline = GlassBorder,
  outlineVariant = GlassHighlight,
  error = SoftCoralError,
  onError = DeepBgTop,
)

private val AppShapes = Shapes(
  small = RoundedCornerShape(12.dp),
  medium = RoundedCornerShape(18.dp),
  large = RoundedCornerShape(24.dp),
  extraLarge = RoundedCornerShape(30.dp),
)

@Composable
fun RadeonFuelTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = ObsidianTrackerColorScheme,
    typography = Typography,
    shapes = AppShapes,
    content = content,
  )
}
