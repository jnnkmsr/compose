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

import android.os.Build
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * [CompositionLocal] used to specify the default [Easings] for themed
 * animations. If not provided by a theme, it defaults to the default
 * [Material 3](https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration)
 * easing sets.
 */
public val LocalEasings: ProvidableCompositionLocal<Easings> =
    staticCompositionLocalOf { Easings() }

/** Holds [Easing]s that can be provided by a theme. */
@Immutable
public class Easings(
    /**
     * [Easing] for non-emphasized on-screen transitions. Defaults to the
     * [Material 3](https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration)
     * _"standard"_ easing.
     */
    public val standard: Easing = Standard,
    /**
     * [Easing] for non-emphasized enter-screen transitions. Defaults to the
     * [Material 3](https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration)
     * _"standard decelerate"_ easing.
     */
    public val standardEnter: Easing = StandardDecelerate,
    /**
     * [Easing] for non-emphasized exit-screen transitions. Defaults to the
     * [Material 3](https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration)
     * _"standard accelerate"_ easing.
     */
    public val standardExit: Easing = StandardAccelerate,
    /**
     * [Easing] for emphasized on-screen transitions. Defaults to the
     * [Material 3](https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration)
     * _"emphasized"_ easing.
     */
    public val emphasized: Easing = Emphasized,
    /**
     * [Easing] for emphasized enter-screen transitions. Defaults to the
     * [Material 3](https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration)
     * _"emphasized decelerate"_ easing.
     */
    public val emphasizedEnter: Easing = EmphasizedDecelerate,
    /**
     * [Easing] for emphasized exit-screen transitions. Defaults to the
     * [Material 3](https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration)
     * _"emphasized accelerate"_ easing.
     */
    public val emphasizedExit: Easing = EmphasizedAccelerate,
) {
    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is Easings -> false
        standard != other.standard -> false
        standardEnter != other.standardEnter -> false
        standardExit != other.standardExit -> false
        emphasized != other.emphasized -> false
        emphasizedEnter != other.emphasizedEnter -> false
        else -> emphasizedExit == other.emphasizedExit
    }

    override fun hashCode(): Int {
        var result = standard.hashCode()
        result = 31 * result + standardEnter.hashCode()
        result = 31 * result + standardExit.hashCode()
        result = 31 * result + emphasized.hashCode()
        result = 31 * result + emphasizedEnter.hashCode()
        result = 31 * result + emphasizedExit.hashCode()
        return result
    }

    override fun toString(): String =
        "Easings(" +
        "standard=$standard, " +
        "standardEnter=$standardEnter, " +
        "standardExit=$standardExit, " +
        "emphasized=$emphasized, " +
        "emphasizedEnter=$emphasizedEnter, " +
        "emphasizedExit=$emphasizedExit)"

    /**
     * Holder for the default
     * [Material 3 easings](https://m3.material.io/styles/motion/easing-and-duration/).
     */
    public companion object {

        /**
         * Easing for on-screen transitions from the
         * [standard easing set](http://m3.material.io/styles/motion/easing-and-duration/tokens-specs#601d5552-a6e6-4d74-9886-ff8f24b9ec35).
         */
        public val Standard: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)

        /**
         * Easing for exit transitions from the
         * [standard easing set](http://m3.material.io/styles/motion/easing-and-duration/tokens-specs#601d5552-a6e6-4d74-9886-ff8f24b9ec35).
         */
        public val StandardAccelerate: Easing = CubicBezierEasing(0.3f, 0f, 1f, 1f)

        /**
         * Easing for enter transitions from the
         * [standard easing set](http://m3.material.io/styles/motion/easing-and-duration/tokens-specs#601d5552-a6e6-4d74-9886-ff8f24b9ec35).
         */
        public val StandardDecelerate: Easing = CubicBezierEasing(0f, 0f, 0f, 1f)

        /**
         * Easing for on-screen transitions from the
         * [emphasized easing set](http://m3.material.io/styles/motion/easing-and-duration/tokens-specs#cbea5c6e-7b0d-47a0-98c3-767080a38d95).
         *
         * Requires an API level ≥ 26. For older Android versions, the [Standard]
         * easing is used instead.
         */
        public val Emphasized: Easing =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PathEasing(
                    0.05f, 0f,
                    0.133333f, 0.06f,
                    0.166666f, 0.4f,
                    0.208333f, 0.82f,
                    0.25f, 1f,
                )
            } else {
                Standard
            }

        /**
         * Easing for exit transitions from the
         * [emphasized easing set](http://m3.material.io/styles/motion/easing-and-duration/tokens-specs#cbea5c6e-7b0d-47a0-98c3-767080a38d95).
         */
        public val EmphasizedAccelerate: Easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

        /**
         * Easing for enter transitions from the
         * [emphasized easing set](http://m3.material.io/styles/motion/easing-and-duration/tokens-specs#cbea5c6e-7b0d-47a0-98c3-767080a38d95).
         */
        public val EmphasizedDecelerate: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    }
}
