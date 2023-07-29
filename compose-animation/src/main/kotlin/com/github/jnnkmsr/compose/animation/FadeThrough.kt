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

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

/**
 * Returns the [EnterTransition] of a fade through transition with the given
 * total transition [duration]. The transition is delayed to allow for fade out
 * to complete, such that the duration of the [EnterTransition] is given by
 * `(duration * (1f - crossing))`.
 *
 * @param duration The duration of the entire fade through transition
 *   (milliseconds).
 * @param delay An optional additional delay before starting the transition
 *   (milliseconds).
 * @param crossing The relative crossing point between the fade in and fade out
 *   segments of the transition (`0.3f` by default). This should be a floating
 *   point number between `0f` and `1f`.
 * @param easing The [Easing] applied to the [EnterTransition].
 * @param initialAlpha The starting alpha of the fade transition (`0f` by
 *   default).
 * @param scaleIn If `true`, an additional scaling animation is applied (`false`
 *   by default).
 * @param initialScale The initial scale for the optional scale transition
 *   (`0.92f` by default). Only applied if [scaleIn] is set to `true`.
 * @param transformOrigin The pivot point of the optional scale transition
 *   ([TransformOrigin.Center] by default). Only applied if [scaleIn] is set to
 *   `true`.
 */
@Composable
@ReadOnlyComposable
public fun fadeThroughEnter(
    duration: Int = AnimationDefaults.EnterDuration,
    delay: Int = 0,
    crossing: Float = AnimationDefaults.FadeThroughCrossing,
    easing: Easing = AnimationDefaults.EnterEasing,
    initialAlpha: Float = 0f,
    scaleIn: Boolean = false,
    initialScale: Float = AnimationDefaults.FadeInScale,
    transformOrigin: TransformOrigin = TransformOrigin.Center,
): EnterTransition {
    val fadeOutDuration = (duration * crossing).roundToInt()
    val fadeInDuration = duration - fadeOutDuration
    val fadeInDelay = delay + fadeOutDuration
    val animationSpec = tween<Float>(fadeInDuration, fadeInDelay, easing)

    return fadeIn(animationSpec, initialAlpha) +
           // The additional expandIn transition delays the appearance of the
           // bounding box until fade out has completed.
           expandIn(snap(fadeInDelay)) { IntSize.Zero } +
           if (scaleIn) {
               scaleIn(animationSpec, initialScale, transformOrigin)
           } else {
               EnterTransition.None
           }
}

/**
 * Returns the [ExitTransition] of a fade through transition with the given
 * total transition [duration]. The duration of the [ExitTransition] is given
 * by *([duration] * [crossing])*.
 *
 * @param duration The duration of the entire fade through transition
 *   (milliseconds).
 * @param delay An optional delay before starting the transition (milliseconds).
 * @param crossing The relative crossing point between the fade in and fade out
 *   segments of the transition (`0.3f` by default). This should be a floating
 *   point number between `0f` and `1f`.
 * @param easing The [Easing] applied to the [EnterTransition].
 * @param targetAlpha The final alpha of the fade transition (`0f` by default).
 */
@Composable
@ReadOnlyComposable
public fun fadeThroughExit(
    duration: Int = AnimationDefaults.ExitDuration,
    delay: Int = 0,
    crossing: Float = AnimationDefaults.FadeThroughCrossing,
    easing: Easing = AnimationDefaults.ExitEasing,
    targetAlpha: Float = 0f,
): ExitTransition {
    val fadeOutDuration = (duration * crossing).roundToInt()
    return fadeOut(tween(fadeOutDuration, delay, easing), targetAlpha)
}
