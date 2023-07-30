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

package com.github.jnnkmsr.compose.icon.token

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import com.github.jnnkmsr.compose.icon.IconSize

/** Holder for [IconSize] default values. */
@Immutable
internal object IconToken {
    val DefaultIconSize = 24.dp
    val DefaultStateLayerSize = 40.dp
    val DefaultTouchTargetSize = 48.dp
    val LargeIconSize = 32.dp
    val LargeStateLayerSize = 60.dp
    val LargeTouchTargetSize = 72.dp
    val SmallIconSize = 20.dp
    val SmallStateLayerSize = 28.dp
    val SmallTouchTargetSize = 36.dp
    val TinyIconSize = 16.dp
    val TinyStateLayerSize = 24.dp
    val TinyTouchTargetSize = 32.dp
}
