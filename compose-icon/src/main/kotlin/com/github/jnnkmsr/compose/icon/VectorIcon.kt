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

package com.github.jnnkmsr.compose.icon

import android.animation.ObjectAnimator
import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

/**
 * Wrapper for UI icons of different kinds (drawables, image vectors, animated
 * vector drawables), allowing to use all icons with a common API.
 */
@Immutable
public sealed interface VectorIcon

/**
 * [StaticVectorIcon] wrapper for single-color
 * [vector-drawable](https://d.android.com/develop/ui/views/graphics/vector-drawable-resources)
 * resource icons that are drawn with a [Painter] or [ImageVector] that can be tinted.
 */
public sealed interface StaticVectorIcon : VectorIcon

/**
 * Returns a [StaticVectorIcon] loading a `drawable` resource with the given [id]
 * and providing a [Painter] for drawing the icon.
 *
 * @param id The resource reference of the vector drawable.
 */
public fun StaticVectorIcon(@DrawableRes id: Int): StaticVectorIcon =
    DrawableVectorIcon(id)

/**
 * Returns a [StaticVectorIcon] using the given [imageVector], obtained, _e.g._,
 * from the [Material `Icons`](http://d.android.com/reference/kotlin/androidx/compose/material/icons/package-summary.html)
 * collection.
 */
public fun StaticVectorIcon(imageVector: ImageVector): StaticVectorIcon =
    ImageVectorIcon(imageVector)

/**
 * [StaticVectorIcon] implementation loading a `drawable` resource with the given
 * [id] and providing a [Painter] for drawing the icon.
 */
@JvmInline
internal value class DrawableVectorIcon(
    /** The resource reference of the vector drawable. */
    @DrawableRes private val id: Int,
) : StaticVectorIcon {

    /**
     * Creates and returns a [Painter] from the resource [id] that is remembered
     * across compositions.
     *
     * @see painterResource
     */
    internal val painter
        @Composable
        get() = painterResource(id)
}

/**
 * [StaticVectorIcon] implementation using a pre-built [ImageVector], _e.g._,
 * obtained from the [Material `Icons`](http://d.android.com/reference/kotlin/androidx/compose/material/icons/package-summary.html)
 * collections.
 */
@JvmInline
internal value class ImageVectorIcon(
    /** The [ImageVector] graphics object. */
    internal val imageVector: ImageVector,
) : StaticVectorIcon

/**
 * [StaticVectorIcon] wrapper for
 * [animated vector-drawable (AVD)](https://d.android.com/develop/ui/views/animations/drawable-animation#AnimVector)
 * resource icons that are drawn with a [Painter] that can be animated between
 * the icon's initial and final state.
 */
public data class AnimatedVectorIcon(
    /** The resource reference of the animated vector drawable. */
    @DrawableRes private val id: Int,
    /**
     * The duration of the icon animation, as given by the animated
     * vector-drawable (AVD) resource.
     *
     * The passed value should match the total duration of the [ObjectAnimator]s
     * driving the AVD to correctly infer when an icon transition has completed.
     */
    public val durationMillis: Int,
) : VectorIcon {

    /**
     * Creates and returns a [Painter] from the resource [id] that is remembered
     * across compositions. The [Painter] renders an [AnimatedImageVector] that
     * draw the icon either in its initial or final state, depending on the
     * value of [atEnd], with changed being animated.
     *
     * @param atEnd Whether the animated vector should be rendered in its final
     *   state.
     * @see rememberAnimatedVectorPainter
     */
    @ExperimentalAnimationGraphicsApi
    @Composable
    internal fun painter(atEnd: Boolean): Painter =
        rememberAnimatedVectorPainter(
            animatedImageVector = AnimatedImageVector.animatedVectorResource(id),
            atEnd = atEnd,
        )
}
