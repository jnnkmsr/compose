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
import androidx.compose.animation.core.TweenSpec
import kotlin.math.roundToInt

/**
 * Convenience function returning a [TweenSpec] with [duration] and [delay]
 * being calculated relative to the given [totalDurationMillis].
 *
 * @param totalDurationMillis The total duration of the animation in milliseconds.
 * @param delay The relative delay as a fraction of [totalDurationMillis]
 *   (default: `0f`).
 * @param duration The relative duration as a fraction of [totalDurationMillis]
 *   (default: `1f-`[delay]).
 * @param easing The [Easing] curve that will be used to interpolate between
 *   start and end.
 */
public fun <T> tween(
    totalDurationMillis: Int,
    delay: Float = 0f,
    duration: Float = 1f - delay,
    easing: Easing = EasingDefaults.Standard,
): TweenSpec<T> = androidx.compose.animation.core.tween(
    durationMillis = (totalDurationMillis * duration).roundToInt(),
    delayMillis = (totalDurationMillis * delay).roundToInt(),
    easing = easing,
)
