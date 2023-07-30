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
import com.github.jnnkmsr.compose.animation.Durations
import com.github.jnnkmsr.compose.theme.Theme

/**
 * [Design tokens](http://m3.material.io/foundations/design-tokens/overview)
 * for [Durations] values.
 *
 * Tokens are used to reference theme values. To convert a token into a theme
 * animation duration, use [toDuration].
 */
@Immutable
public enum class DurationToken {
    ShortEnter,
    ShortExit,
    MediumEnter,
    MediumExit,
    LongEnter,
    LongExit,
}

/**
 * Converts a [DurationToken] to the local theme animation duration.
 */
@ReadOnlyComposable
@Composable
public fun DurationToken.toDuration(): Int = Theme.durations.toDuration(this)

/**
 * Converts a [DurationToken] into a theme animation duration values using
 * `this` themed [Durations].
 */
private fun Durations.toDuration(token: DurationToken): Int = when (token) {
    DurationToken.ShortEnter -> shortEnter
    DurationToken.ShortExit -> shortExit
    DurationToken.MediumEnter -> mediumEnter
    DurationToken.MediumExit -> mediumExit
    DurationToken.LongEnter -> longEnter
    DurationToken.LongExit -> longExit
}
