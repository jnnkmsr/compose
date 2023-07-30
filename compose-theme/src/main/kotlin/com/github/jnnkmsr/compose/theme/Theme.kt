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

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.github.jnnkmsr.compose.animation.Durations
import com.github.jnnkmsr.compose.animation.Easings
import com.github.jnnkmsr.compose.animation.LocalDurations
import com.github.jnnkmsr.compose.animation.LocalEasings

/**
 * Gives access the current theme values provided at the call site's position
 * in the hierarchy.
 */
public object Theme {

    /**
     * Retrieves the current [ColorScheme] at the call site's position in the
     * hierarchy.
     */
    public val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    /**
     * Retrieves the current [Alpha] opacity values at the call site's position
     * in the hierarchy.
     */
    public val alpha: Alpha
        @Composable
        @ReadOnlyComposable
        get() = LocalAlpha.current

    /**
     * Retrieves the current [Shapes] at the call site's position in the
     * hierarchy.
     */
    public val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    /**
     * Retrieves the current [Typography] at the call site's position in the
     * hierarchy.
     */
    public val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Retrieves the current animation [Durations] at the call site's position
     * in the hierarchy.
     */
    public val durations: Durations
        @Composable
        @ReadOnlyComposable
        get() = LocalDurations.current

    /**
     * Retrieves the current animation [Easings] at the call site's position
     * in the hierarchy.
     */
    public val easings: Easings
        @Composable
        @ReadOnlyComposable
        get() = LocalEasings.current
}

/**
 * Provides an extended [MaterialTheme].
 *
 * @param lightColorScheme The [ColorScheme] that will be provided by the theme
 *   while [useDarkTheme] is `false`. Will be ignored if [useDynamicColor] is
 *   set to `true`.
 * @param darkColorScheme The [ColorScheme] that will be provided by the theme
 *   while [useDarkTheme] is `true`. Will be ignored if [useDynamicColor] is
 *   set to `true`.
 * @param useDarkTheme Whether the theme should use a the [darkColorScheme]
 *   (follows the system setting by default).
 * @param useDynamicColor If `true` and supported by the Android system, enables
 *   [dynamic color](https://m3.material.io/styles/color/dynamic-color),
 *   ignoring the schemes set by [lightColorScheme] and [darkColorScheme].
 *   If `false`, disables the use of dynamic theming, even when it is supported.
 * @param alpha The [Alpha] opacity values that will be provided by the theme.
 * @param shapes The [Shapes] that will be provided by the theme.
 * @param shapes The [Typography] that will be provided by the theme.
 * @param durations The animation [Durations] that will be provided by the theme.
 * @param easings The animation [Easings] that will be provided by the theme.
 * @param content The composable content the theme is applied to.
 */
@Composable
public fun Theme(
    lightColorScheme: ColorScheme = lightColorScheme(),
    darkColorScheme: ColorScheme = darkColorScheme(),
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = true,
    alpha: Alpha = Alpha(),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    durations: Durations = Durations(),
    easings: Easings = Easings(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    Theme(
        colorScheme = remember(context, useDynamicColor, useDarkTheme) {
            val enableDynamicColor = useDynamicColor && supportsDynamicTheming()
            when {
                enableDynamicColor && useDarkTheme -> dynamicDarkColorScheme(context)
                enableDynamicColor -> dynamicLightColorScheme(context)
                useDarkTheme -> darkColorScheme
                else -> lightColorScheme
            }
        },
        alpha = alpha,
        shapes = shapes,
        typography = typography,
        durations = durations,
        easings = easings,
        content = content,
    )
}

/**
 * Internal theming function providing the theme inputs via [MaterialTheme].
 *
 * @param colorScheme The [ColorScheme] that will be provided by `LocalColorScheme`.
 * @param alpha The [Alpha] values that will be provided by [LocalAlpha].
 * @param shapes The [Shapes] that will be provided by `LocalShapes`.
 * @param typography The [Typography] that will be provided by `LocalTypography`.
 * @param durations The animation [Durations] that will be provided by [LocalDurations].
 * @param durations The animation [Easings] that will be provided by [LocalEasings].
 * @param content The composable content the theme is applied to.
 */
@Composable
private fun Theme(
    colorScheme: ColorScheme,
    alpha: Alpha,
    shapes: Shapes,
    typography: Typography,
    durations: Durations,
    easings: Easings,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAlpha provides alpha,
        LocalDurations provides durations,
        LocalEasings provides easings,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = typography,
            content = content,
        )
    }
}

/**
 * Helper function to check if the SDK supports dynamic theming.
 */
@ChecksSdkIntAtLeast(Build.VERSION_CODES.S)
private fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
