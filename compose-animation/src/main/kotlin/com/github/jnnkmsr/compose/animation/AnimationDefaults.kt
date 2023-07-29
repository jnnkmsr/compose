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

package com.github.jnnkmsr.compose.animation

import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable

/** Holder for animation constants and default values. */
@Immutable
internal object AnimationDefaults {

    internal val EnterDuration: Int
        @Composable
        @ReadOnlyComposable
        get() = LocalDurations.current.mediumEnter

    internal val ExitDuration: Int
        @Composable
        @ReadOnlyComposable
        get() = LocalDurations.current.mediumExit

    internal val EnterEasing: Easing
        @Composable
        @ReadOnlyComposable
        get() = LocalEasings.current.standardEnter

    internal val ExitEasing: Easing
        @Composable
        @ReadOnlyComposable
        get() = LocalEasings.current.standardExit

    internal const val FadeInScale: Float = 0.92f

    internal const val FadeThroughCrossing: Float = 0.3f
}
