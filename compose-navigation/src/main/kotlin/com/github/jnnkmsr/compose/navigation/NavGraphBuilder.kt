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
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder

/**
 * Add a [ComposableScreen] to the [NavGraphBuilder].
 *
 * @param screen The [ComposableScreen] to be added.
 * @param enter Callback to determine the destination's `enter` transition. If
 * no non-null value is provided, the node's [enter][NavScreen.enter] spec is used
 * instead.
 * @param exit Callback to determine the destination's `exit` transition. If
 * no non-null value is provided, the node's [exit][NavScreen.exit] spec is used
 * instead.
 * @param popEnter Callback to determine the destination's `popEnter` transition. If
 * no non-null value is provided, the node's [popEnter][NavScreen.popEnter] spec is used
 * instead.
 * @param popExit Callback to determine the destination's `popExit` transition. If
 * no non-null value is provided, the node's [popExit][NavScreen.popExit] spec is used
 * instead.
 * @param content The composable content for the destination
 */
public fun NavGraphBuilder.composable(
    screen: ComposableScreen,
    enter: NavEnterSpec? = null,
    exit: NavExitSpec? = null,
    popEnter: NavEnterSpec? = enter,
    popExit: NavExitSpec? = exit,
    content: NavContent,
) {
    with(screen) {
        composable(enter, exit, popEnter, popExit, content)
    }
}

/**
 * Construct a nested [NavGraph]
 *
 * @param screen The [NestedGraphScreen] associated with the graph.
 * @param enter Callback defining the default `enter` transition for destinations
 * in this graph. If no non-null value is provided, the node's [enter][NavScreen.enter]
 * spec is used instead.
 * @param exit Callback defining the default `exit` transition for destinations
 * in this graph. If no non-null value is provided, the node's [exit][NavScreen.exit]
 * spec is used instead.
 * @param popEnter Callback defining the default `popEnter` transition for destinations
 * in this graph. If no non-null value is provided, the node's [popEnter][NavScreen.popEnter]
 * spec is used instead.
 * @param popExit Callback defining the default `popExit` transition for destinations
 * in this graph. If no non-null value is provided, the node's [popExit][NavScreen.popExit]
 * spec is used instead.
 * @param builder The builder used to construct the graph.
 */
public fun NavGraphBuilder.navigation(
    screen: NestedGraphScreen,
    enter: NavEnterSpec? = null,
    exit: NavExitSpec? = null,
    popEnter: NavEnterSpec? = enter,
    popExit: NavExitSpec? = exit,
    builder: NavGraphBuilder.() -> Unit,
) {
    with(screen) {
        navigation(enter, exit, popEnter, popExit, builder)
    }
}

/**
 * Adds the given [feature] to the navigation graph.
 *
 * @param feature The [NavFeature] to be added to the graph.
 * @param enter Callback to determine the feature's `enter` transition.
 * If no non-null value is provided, the node's [enter][NavScreen.enter] spec is
 * used instead.
 * @param exit Callback to determine the feature's `exit` transition.
 * If no non-null value is provided, the node's [exit][NavScreen.exit] spec is
 * used instead.
 * @param popEnter Callback to determine the feature's `popEnter` transition.
 * If no non-null value is provided, the node's [popEnter][NavScreen.popEnter]
 * spec is used instead.
 * @param popExit Callback to determine the feature's `popExit` transition.
 * If no non-null value is provided, the node's [popExit][NavScreen.popExit] spec
 * is used instead.
 */
public fun NavGraphBuilder.feature(
    feature: NavFeature,
    enter: NavEnterSpec? = null,
    exit: NavExitSpec? = null,
    popEnter: NavEnterSpec? = enter,
    popExit: NavExitSpec? = exit,
) {
    with(feature) {
        feature(enter, exit, popEnter, popExit)
    }
}

/**
 * Adds a [NavDialog] to the navigation graph.
 *
 * @param dialog The [NavDialog] to be added to the graph.
 * @param enter The [EnterTransition] for this dialog. If no non-null value
 *   is provided, the default [enter][NavDialog.enter] is used instead.
 * @param exit The [ExitTransition] for this dialog. If no non-null value
 *   is provided, the default [exit][NavDialog.exit] is used instead.
 */
public fun NavGraphBuilder.dialog(
    dialog: NavDialog,
    enter: EnterTransition? = null,
    exit: ExitTransition? = null,
) {
    with(dialog) {
        dialog(enter, exit)
    }
}
