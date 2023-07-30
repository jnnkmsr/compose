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

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A [Material Design icon](https://m3.material.io/styles/icons/overview)
 * component taking a [StaticVectorIcon] as input.
 *
 * If [icon] is a [StaticVectorIcon], the static vector icon will be displayed
 * using the given [tint]. If no [tint] is specified, the current default value
 * from [LocalContentColor] will be used.
 *
 * If [icon] is an [AnimatedVectorIcon], an animated vector icon will be
 * displayed. The animation state is animated between its initial and final
 * state depending on the given value of [atEnd].
 *
 * @param icon The [StaticVectorIcon] that is displayed.
 * @param contentDescription Text used by accessibility services to describe
 *   what this icon represents. This should always be provided unless this icon
 *   is used for decorative purposes, and does not represent a meaningful action
 *   that a user can take. The text should be localized, such as by using
 *   `stringResource` or similar.
 * @param modifier The [Modifier] to be applied to the icon container.
 * @param atEnd Only used if the [icon] is an [AnimatedVectorIcon], indicating
 *   whether the animated icon is at its end state.
 * @param size The [IconSize] setting the size of the icon. Defaults to the
 *   current value of [LocalIconSize], which is `null` by default, meaning that
 *   the icon will be drawn at its natural size.
 * @param tint Tint to be applied to the [icon]. If [Color.Unspecified] is
 *   provided, then no tint is applied. If [tint] is not specified that the
 *   current [LocalContentColor] will be applied.
 */
@ExperimentalAnimationGraphicsApi
@Composable
public fun Icon(
    icon: VectorIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    atEnd: Boolean = false,
    size: IconSize? = LocalIconSize.current,
    tint: Color = LocalContentColor.current,
) {
    val sizeModifier = size
        ?.let { Modifier.size(size.imageSize) }
        ?: Modifier

    when (icon) {
        is ImageVectorIcon -> androidx.compose.material3.Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = modifier.then(sizeModifier),
            tint = tint,
        )
        is DrawableVectorIcon -> androidx.compose.material3.Icon(
            painter = icon.painter,
            contentDescription = contentDescription,
            modifier = modifier.then(sizeModifier),
            tint = tint,
        )
        is AnimatedVectorIcon -> androidx.compose.material3.Icon(
            painter = icon.painter(atEnd),
            contentDescription = contentDescription,
            modifier = modifier.then(sizeModifier),
            tint = tint,
        )
    }
}
