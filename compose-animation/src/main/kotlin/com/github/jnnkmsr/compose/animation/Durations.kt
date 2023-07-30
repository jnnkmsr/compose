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

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * [CompositionLocal] used to specify the default [Durations] for themed
 * animations. If not provided by a theme, default durations will be provided.
 */
public val LocalDurations: ProvidableCompositionLocal<Durations> =
    staticCompositionLocalOf { Durations() }

/** Holds animation durations that can be provided by a theme. */
@Immutable
public class Durations(
    /**
     * Animation duration to be used for short enter transitions. Defaults
     * to 150 milliseconds.
     */
    public val shortEnter: Int = DurationDefaults.ShortEnter,
    /**
     * Animation duration to be used for short exit transitions. Defaults
     * to 100 milliseconds.
     */
    public val shortExit: Int = DurationDefaults.ShortExit,
    /**
     * Animation duration to be used for medium-long enter transitions.
     * Defaults to 300 milliseconds.
     */
    public val mediumEnter: Int = DurationDefaults.MediumEnter,
    /**
     * Animation duration to be used for medium-long exit transitions.
     * Defaults to 200 milliseconds.
     */
    public val mediumExit: Int = DurationDefaults.MediumExit,
    /**
     * Animation duration to be used for long enter transitions. Defaults
     * to 600 milliseconds.
     */
    public val longEnter: Int = DurationDefaults.LongEnter,
    /**
     * Animation duration to be used for long exit transitions. Defaults
     * to 400 milliseconds.
     */
    public val longExit: Int = DurationDefaults.LongExit,
) {
    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is Durations -> false
        shortEnter != other.shortEnter -> false
        shortExit != other.shortExit -> false
        mediumEnter != other.mediumEnter -> false
        mediumExit != other.mediumExit -> false
        longEnter != other.longEnter -> false
        else -> longExit == other.longExit
    }

    override fun hashCode(): Int {
        var result = shortEnter
        result = 31 * result + shortExit
        result = 31 * result + mediumEnter
        result = 31 * result + mediumExit
        result = 31 * result + longEnter
        result = 31 * result + longExit
        return result
    }

    override fun toString(): String =
        "Durations(" +
        "shortEnter=$shortEnter, " +
        "shortExit=$shortExit, " +
        "mediumEnter=$mediumEnter, " +
        "mediumExit=$mediumExit, " +
        "longEnter=$longEnter, " +
        "longExit=$longExit)"

}

/** Holder for default [Durations] values. */
private data object DurationDefaults {
    const val ShortEnter = 150
    const val ShortExit = 100
    const val MediumEnter = 300
    const val MediumExit = 200
    const val LongEnter = 600
    const val LongExit = 400
}
