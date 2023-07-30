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

import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import com.github.jnnkmsr.compose.animation.Easings
import com.github.jnnkmsr.compose.theme.Theme

/**
 * [Design tokens](http://m3.material.io/foundations/design-tokens/overview)
 * for [Easings] values.
 *
 * Tokens are used to reference theme values. To convert a token into a theme
 * [Easing] value, use [toEasing].
 */
@Immutable
public enum class EasingToken {
    Standard,
    StandardEnter,
    StandardExit,
    Emphasized,
    EmphasizedEnter,
    EmphasizedExit,
}

/**
 * Converts an [EasingToken] to the local theme animation [Easing].
 */
@ReadOnlyComposable
@Composable
public fun EasingToken.toEasing(): Easing = Theme.easings.toEasing(this)

/**
 * Converts an [EasingToken] into a theme [Easing] using `this` themed [Easings].
 */
private fun Easings.toEasing(token: EasingToken): Easing = when (token) {
    EasingToken.Standard -> standard
    EasingToken.StandardEnter -> standardEnter
    EasingToken.StandardExit -> standardExit
    EasingToken.Emphasized -> emphasized
    EasingToken.EmphasizedEnter -> emphasizedEnter
    EasingToken.EmphasizedExit -> emphasizedExit
}
