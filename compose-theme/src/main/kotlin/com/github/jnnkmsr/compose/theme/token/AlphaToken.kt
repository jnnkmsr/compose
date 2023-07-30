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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import com.github.jnnkmsr.compose.theme.Alpha
import com.github.jnnkmsr.compose.theme.Theme

/**
 * [Design tokens](http://m3.material.io/foundations/design-tokens/overview)
 * for [Alpha] values.
 *
 * Tokens are used to reference theme values. To convert a token into a theme
 * opacity `alpha` value, use [toAlpha].
 */
@Immutable
public enum class AlphaToken {
    Disabled,
    DisabledOutline,
    Full,
    Low,
    Scrim,
}

/**
 * Converts an [AlphaToken] to the local theme `alpha` opacity value.
 */
@ReadOnlyComposable
@Composable
public fun AlphaToken.toAlpha(): Float = Theme.alpha.toAlpha(this)

/**
 * Converts an [AlphaToken] into a theme opacity `alpha` value using `this`
 * themed [Alpha].
 */
private fun Alpha.toAlpha(token: AlphaToken): Float = when (token) {
    AlphaToken.Disabled -> disabled
    AlphaToken.DisabledOutline -> disabledOutline
    AlphaToken.Full -> full
    AlphaToken.Low -> low
    AlphaToken.Scrim -> scrim
}
