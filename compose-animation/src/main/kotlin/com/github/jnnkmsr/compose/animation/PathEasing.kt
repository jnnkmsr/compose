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

import android.graphics.Path
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.lerp

/**
 * Creates an easing of two cubic Bézier curves from `(0,0)` to `(x3,y3)` with
 * control points `(x1,y1)` and `(x2,y2)`, and from `(x3,y3)` to `(1,1)` with
 * the control points `(x4,y4)` and `(x5,y5)`.
 *
 * @param x1 The x coordinate of the first Bézier segment's first control point.
 * @param y1 The y coordinate of the first Bézier segment's first control point.
 * @param x2 The x coordinate of the first Bézier segment's second control point.
 * @param y2 The y coordinate of the first Bézier segment's second control point.
 * @param x3 The x coordinate of the first Bézier segment's end point and the
 * second Bézier segment's start point.
 * @param y3 The y coordinate of the first Bézier segment's end point and the
 * second Bézier segment's start point.
 * @param x4 The x coordinate of the second Bézier segment's first control point.
 * @param y4 The y coordinate of the second Bézier segment's first control point.
 * @param x5 The x coordinate of the second Bézier segment's second control point.
 * @param y5 The y coordinate of the second Bézier segment's second control point.
 */
@RequiresApi(Build.VERSION_CODES.O)
public fun PathEasing(
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    x3: Float,
    y3: Float,
    x4: Float,
    y4: Float,
    x5: Float,
    y5: Float,
): PathEasing = PathEasing(
    Path().apply {
        moveTo(0f, 0f)
        cubicTo(x1, y1, x2, y2, x3, y3)
        cubicTo(x4, y4, x5, y5, 1f, 1f)
    }
)

/**
 * An easing that can traverse a [Path] that extends from point `(0,0)` to
 * `(1,1)`, equivalent to a [PathInterpolator][android.view.animation.PathInterpolator].
 */
@Immutable
@RequiresApi(Build.VERSION_CODES.O)
public class PathEasing private constructor() : Easing {

    /**
     * Creates a [PathEasing] from the given [path]. The path must extend from
     * `(0,0)` to `(1,1)` and must not have any discontinuities in the *x*-axis.
     */
    internal constructor(path: Path) : this() {
        initPath(path)
    }

    /**
     * *x* coordinates of approximated pre-calculated points along the path.
     */
    private lateinit var x: FloatArray

    /**
     * *y* coordinates of approximated pre-calculated points along the path.
     */
    private lateinit var y: FloatArray

    /**
     * Called by constructors to initialize this easing with the given [path].
     * Approximates the path with a series of line segments, stored as [x] and
     * [y] coordinate arrays, simplifying the easing [transform] to a linear
     * interpolation.
     */
    private fun initPath(path: Path) {
        val pointComponents = path.approximate(PRECISION)

        @Suppress("MagicNumber")
        val numPoints = pointComponents.size / 3
        x = FloatArray(numPoints)
        y = FloatArray(numPoints)

        var previousX = 0f
        var previousFraction = 0f
        var componentIndex = 0
        for (index in 0 until numPoints) {
            val currentFraction = pointComponents[componentIndex++]
            val currentX = pointComponents[componentIndex++]
            val currentY = pointComponents[componentIndex++]
            when {
                currentFraction == previousFraction && currentX != previousX -> {
                    throw IllegalArgumentException(
                        "A PathEasing cannot have a discontinuity in the x axis."
                    )
                }
                currentX < previousX -> {
                    throw IllegalArgumentException(
                        "A PathEasing cannot loop back on itself."
                    )
                }
            }
            x[index] = currentX
            y[index] = currentY
            previousX = currentX
            previousFraction = currentFraction
        }
    }

    override fun transform(fraction: Float): Float {
        when {
            fraction <= 0f -> return 0f
            fraction >= 1f -> return 1f
        }
        var startIndex = 0
        var endIndex = x.size - 1

        while (endIndex - startIndex > 1) {
            val midIndex = (startIndex + endIndex) / 2
            if (fraction < x[midIndex]) {
                endIndex = midIndex
            } else {
                startIndex = midIndex
            }
        }

        return when (val xRange = x[endIndex] - x[startIndex]) {
            0f -> y[startIndex]
            else -> lerp(
                start = y[startIndex],
                stop = y[endIndex],
                fraction = (fraction - x[startIndex]) / xRange
            )
        }
    }

    private companion object {
        private const val PRECISION = 0.002f
    }
}
