/*
 * Copyright 2023 Jannik Möser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jnnkmsr.compose.theme.token

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.github.jnnkmsr.compose.theme.Theme

/**
 * Material 3 [design token](http://m3.material.io/foundations/design-tokens/overview)
 * for [ColorScheme] values.
 *
 * Tokens are used to reference theme values. To convert a token into a scheme
 * [Color], use [toColor].
 */
@Immutable
public enum class ColorToken {
    Background,
    Error,
    ErrorContainer,
    InverseOnSurface,
    InversePrimary,
    InverseSurface,
    OnBackground,
    OnError,
    OnErrorContainer,
    OnPrimary,
    OnPrimaryContainer,
    OnSecondary,
    OnSecondaryContainer,
    OnSurface,
    OnSurfaceVariant,
    OnTertiary,
    OnTertiaryContainer,
    Outline,
    OutlineVariant,
    Primary,
    PrimaryContainer,
    Scrim,
    Secondary,
    SecondaryContainer,
    Surface,
    SurfaceBright,
    SurfaceContainer,
    SurfaceContainerHigh,
    SurfaceContainerHighest,
    SurfaceContainerLow,
    SurfaceContainerLowest,
    SurfaceDim,
    SurfaceTint,
    SurfaceVariant,
    Tertiary,
    TertiaryContainer,
    Transparent,
}

/**
 * Converts a [ColorToken] to the local theme [Color].
 */
@ReadOnlyComposable
@Composable
public fun ColorToken.toColor(): Color = Theme.colorScheme.toColor(this)

/**
 * Converts a [ColorToken] into a scheme [Color] using `this` themed
 * [ColorScheme].
 */
private fun ColorScheme.toColor(token: ColorToken): Color = when (token) {
    ColorToken.Background -> background
    ColorToken.Error -> error
    ColorToken.ErrorContainer -> errorContainer
    ColorToken.InverseOnSurface -> inverseOnSurface
    ColorToken.InversePrimary -> inversePrimary
    ColorToken.InverseSurface -> inverseSurface
    ColorToken.OnBackground -> onBackground
    ColorToken.OnError -> onError
    ColorToken.OnErrorContainer -> onErrorContainer
    ColorToken.OnPrimary -> onPrimary
    ColorToken.OnPrimaryContainer -> onPrimaryContainer
    ColorToken.OnSecondary -> onSecondary
    ColorToken.OnSecondaryContainer -> onSecondaryContainer
    ColorToken.OnSurface -> onSurface
    ColorToken.OnSurfaceVariant -> onSurfaceVariant
    ColorToken.SurfaceTint -> surfaceTint
    ColorToken.OnTertiary -> onTertiary
    ColorToken.OnTertiaryContainer -> onTertiaryContainer
    ColorToken.Outline -> outline
    ColorToken.OutlineVariant -> outlineVariant
    ColorToken.Primary -> primary
    ColorToken.PrimaryContainer -> primaryContainer
    ColorToken.Scrim -> scrim
    ColorToken.Secondary -> secondary
    ColorToken.SecondaryContainer -> secondaryContainer
    ColorToken.Surface -> surface
    ColorToken.SurfaceBright -> surfaceBright
    ColorToken.SurfaceContainer -> surfaceContainer
    ColorToken.SurfaceContainerHigh -> surfaceContainerHigh
    ColorToken.SurfaceContainerHighest -> surfaceContainerHighest
    ColorToken.SurfaceContainerLow -> surfaceContainerLow
    ColorToken.SurfaceContainerLowest -> surfaceContainerLowest
    ColorToken.SurfaceDim -> surfaceDim
    ColorToken.SurfaceVariant -> surfaceVariant
    ColorToken.Tertiary -> tertiary
    ColorToken.TertiaryContainer -> tertiaryContainer
    ColorToken.Transparent -> Color.Transparent
}
