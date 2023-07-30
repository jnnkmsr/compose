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

package com.github.jnnkmsr.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Material 3 [light color scheme](http://m3.material.io/styles/color/the-color-system/tokens#0d90da19-3d89-4964-9a97-5a024fc6d77b)
 * with [TonalPalette] input.
 */
public fun lightColorScheme(
    primary: TonalPalette,
    secondary: TonalPalette,
    tertiary: TonalPalette,
    error: TonalPalette,
    neutral: TonalPalette,
    neutralVariant: TonalPalette,
): ColorScheme = ColorScheme(
    primary = primary[40],
    primaryContainer = primary[90],
    onPrimary = primary[100],
    onPrimaryContainer = primary[10],
    inversePrimary = primary[80],
    secondary = secondary[40],
    secondaryContainer = secondary[90],
    onSecondary = secondary[100],
    onSecondaryContainer = secondary[10],
    tertiary = tertiary[40],
    tertiaryContainer = tertiary[90],
    onTertiary = tertiary[100],
    onTertiaryContainer = tertiary[10],
    surface = neutral[98],
    surfaceDim = neutral[87],
    surfaceBright = neutral[98],
    surfaceContainerLowest = neutral[100],
    surfaceContainerLow = neutral[96],
    surfaceContainer = neutral[94],
    surfaceContainerHigh = neutral[92],
    surfaceContainerHighest = neutral[90],
    surfaceVariant = neutralVariant[90],
    onSurface = neutral[10],
    onSurfaceVariant = neutralVariant[30],
    inverseSurface = neutral[20],
    inverseOnSurface = neutral[95],
    background = neutral[98],
    onBackground = neutral[10],
    error = error[40],
    errorContainer = error[90],
    onError = error[100],
    onErrorContainer = error[10],
    outline = neutralVariant[50],
    outlineVariant = neutralVariant[80],
    surfaceTint = primary[40],
    scrim = neutral[0],
)

/**
 * Material 3 [dark color scheme](http://m3.material.io/styles/color/the-color-system/tokens#7961fcaf-1342-4fea-a613-b87ddd4434ff)
 * with [TonalPalette] input.
 */
public fun darkColorScheme(
    primary: TonalPalette,
    secondary: TonalPalette,
    tertiary: TonalPalette,
    error: TonalPalette,
    neutral: TonalPalette,
    neutralVariant: TonalPalette,
): ColorScheme = ColorScheme(
    primary = primary[80],
    primaryContainer = primary[30],
    onPrimary = primary[20],
    onPrimaryContainer = primary[90],
    inversePrimary = primary[40],
    secondary = secondary[80],
    secondaryContainer = secondary[30],
    onSecondary = secondary[20],
    onSecondaryContainer = secondary[90],
    tertiary = tertiary[80],
    tertiaryContainer = tertiary[30],
    onTertiary = tertiary[20],
    onTertiaryContainer = tertiary[90],
    surface = neutral[6],
    surfaceDim = neutral[6],
    surfaceBright = neutral[24],
    surfaceContainerLowest = neutral[4],
    surfaceContainerLow = neutral[10],
    surfaceContainer = neutral[12],
    surfaceContainerHigh = neutral[17],
    surfaceContainerHighest = neutral[22],
    surfaceVariant = neutralVariant[30],
    onSurface = neutral[90],
    onSurfaceVariant = neutralVariant[80],
    inverseSurface = neutral[90],
    inverseOnSurface = neutral[20],
    background = neutral[6],
    onBackground = neutral[90],
    error = error[80],
    errorContainer = error[30],
    onError = error[20],
    onErrorContainer = error[90],
    outline = neutralVariant[60],
    outlineVariant = neutralVariant[30],
    surfaceTint = primary[80],
    scrim = neutral[0],
)

/**
 * Utility class to simplify Material 3 color theming by defining
 * [tonal palettes](http://m3.material.io/styles/color/the-color-system/key-colors-tones#a828e350-1551-45e5-8430-eb643e6a7713).
 */
@Immutable
public class TonalPalette private constructor(
    /**
     * Immutable map of colors by tonal values ranging from 0 to 100.
     */
    private val sourceColors: Map<Int, Color>,
) {
    /**
     * Creates a [TonalPalette] from the given [sourceColors].
     *
     * @param sourceColors Comma-separated pairs mapping tonal values (ranging
     *   from 0 to 100) to colors.
     */
    public constructor(
        vararg sourceColors: Pair<Int, Color>,
    ) : this(sourceColors.toMap())

    /**
     * Returns the [Color] with the given [tonalValue] from the palette colors.
     * If no color with the given value has been specified, the returned [Color]
     * is interpolated between the closest defined palette colors.
     */
    public operator fun get(tonalValue: Int): Color =
        when (val value = tonalValue.coerceIn(MinTonalValue, MaxTonalValue)) {
            MinTonalValue -> Color.Black
            MaxTonalValue -> Color.White
            else -> sourceColors[value] ?: interpolate(value)
        }

    /**
     * Finds to the closest tonal values to the given [tonalValue] and interpolates
     * between the respective [sourceColors].
     */
    private fun interpolate(tonalValue: Int): Color {
        var lowValue = MinTonalValue
        var lowColor = Color.Black
        var highValue = MaxTonalValue
        var highColor = Color.White

        sourceColors.forEach { (currentTone, currentColor) ->
            when (currentTone) {
                in (lowValue + 1) until tonalValue -> {
                    lowValue = currentTone
                    lowColor = currentColor
                }
                in (tonalValue + 1) until highValue -> {
                    highValue = currentTone
                    highColor = currentColor
                }
            }
        }

        return lerp(
            start = lowColor,
            stop = highColor,
            fraction = (tonalValue - lowValue).toFloat() / (highValue - lowValue),
        )
    }

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is TonalPalette -> false
        else -> sourceColors == other.sourceColors
    }

    override fun hashCode(): Int = sourceColors.hashCode()

    private companion object {
        private const val MinTonalValue = 0
        private const val MaxTonalValue = 100
    }
}
