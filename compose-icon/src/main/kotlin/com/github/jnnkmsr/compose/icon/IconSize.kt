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

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import com.github.jnnkmsr.compose.icon.token.IconToken

/**
 * A set of icon sizes that can be provided to [Icon] and [AnimatedIcon]
 * composables via [LocalIconSize].
 */
@Immutable
public enum class IconSize(
    /** The [Dp] size of the icon image. */
    public val imageSize: Dp,
    /** The [Dp] size of the touchable icon container within an icon button. */
    public val touchTargetSize: Dp,
    /** The [Dp] size of the state layer within an icon button. */
    public val stateLayerSize: Dp,
) {
    Tiny(
        imageSize = IconToken.TinyIconSize,
        touchTargetSize = IconToken.TinyTouchTargetSize,
        stateLayerSize = IconToken.TinyStateLayerSize,
    ),
    Small(
        imageSize = IconToken.SmallIconSize,
        touchTargetSize = IconToken.SmallTouchTargetSize,
        stateLayerSize = IconToken.SmallStateLayerSize,
    ),
    Default(
        imageSize = IconToken.DefaultIconSize,
        touchTargetSize = IconToken.DefaultTouchTargetSize,
        stateLayerSize = IconToken.DefaultStateLayerSize,
    ),
    Large(
        imageSize = IconToken.LargeIconSize,
        touchTargetSize = IconToken.LargeTouchTargetSize,
        stateLayerSize = IconToken.LargeStateLayerSize,
    ),
}

/**
 * Returns the padding around the icon image with respect to its
 * [touchTargetSize][IconSize.touchTargetSize].
 */
public val IconSize.touchTargetPadding: Dp
    get() = (touchTargetSize - imageSize) / 2

/**
 * Returns the padding around the icon image with respect to its
 * [stateLayerSize][IconSize.stateLayerSize].
 */
public val IconSize.stateLayerPadding: Dp
    get() = (stateLayerSize - imageSize) / 2

/**
 * Composition local providing an [IconSize]. Returns `null` by default,
 * meaning that icons will be drawn at their natural size.
 */
public val LocalIconSize: ProvidableCompositionLocal<IconSize?> =
    compositionLocalOf { null }
