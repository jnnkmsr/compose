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

package com.github.jnnkmsr.compose.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.dp

/** Holder for navigation default values. */
public object NavDefaults {

    /** Default transition duration between screens (milliseconds). */
    private const val TransitionDuration = 600

    /** Default enter duration for dialogs (milliseconds). */
    private const val DialogEnterDuration = 250

    /** Default exit duration for dialogs (milliseconds). */
    private const val DialogExitDuration = 150

    /** Minimum padding added around modal dialogs. */
    internal val DialogOuterPadding = 24.dp

    /**
     * Delay before starting a dialog's enter transition (milliseconds).
     * Workaround for dialogs sliding in from the top by default.
     */
    internal const val DialogEnterDelay = 50L

    /** Default [EnterTransition] for navigation transitions. */
    public val Enter: EnterTransition = fadeIn(tween(TransitionDuration))

    /** Default [ExitTransition] for navigation transitions. */
    public val Exit: ExitTransition = fadeOut(tween(TransitionDuration))

    /** Default [EnterTransition] for dialogs. */
    public val DialogEnter: EnterTransition = fadeIn(tween(DialogEnterDuration))

    /** Default [ExitTransition] for dialogs. */
    public val DialogExit: ExitTransition = fadeOut(tween(DialogExitDuration))
}
