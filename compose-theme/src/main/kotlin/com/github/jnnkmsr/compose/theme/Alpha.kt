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

package com.github.jnnkmsr.compose.theme

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * [CompositionLocal] used to specify the default [Alpha] values for a theme.
 */
internal val LocalAlpha: ProvidableCompositionLocal<Alpha> =
    staticCompositionLocalOf { Alpha() }

/** Holds opacity `alpha` values that can be provided by a theme. */
@Immutable
public class Alpha(
    /** Lowered opacity. Defaults to `0.62f`. */
    public val low: Float = AlphaDefaults.Low,
    /** Opacity for disabled components. Defaults to `0.38f`. */
    public val disabled: Float = AlphaDefaults.Disabled,
    /** Scrim opacity. Defaults to `0.32f`. */
    public val scrim: Float = AlphaDefaults.Scrim,
    /** Opacity for disabled outlines. Defaults to `0.12f`. */
    public val disabledOutline: Float = AlphaDefaults.DisabledOutline,
) {
    /** Full opacity. Always return `1f`. */
    public val full: Float get() = AlphaDefaults.Full

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is Alpha -> false
        low != other.low -> false
        disabled != other.disabled -> false
        scrim != other.scrim -> false
        else -> disabledOutline == other.disabledOutline
    }

    override fun hashCode(): Int {
        var result = low.hashCode()
        result = 31 * result + disabled.hashCode()
        result = 31 * result + scrim.hashCode()
        result = 31 * result + disabledOutline.hashCode()
        return result
    }

    override fun toString(): String =
        "Alpha(" +
        "low=$low, " +
        "disabled=$disabled, " +
        "scrim=$scrim, " +
        "disabledOutline=$disabledOutline)"
}

/** Holder for default [Alpha] values. */
private data object AlphaDefaults {
    const val Disabled = 0.38f
    const val DisabledOutline = 0.12f
    const val Full = 1f
    const val Low = 0.62f
    const val Scrim = 0.32f
}
