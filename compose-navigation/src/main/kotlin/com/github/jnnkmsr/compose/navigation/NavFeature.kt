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

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder

/**
 * Supertype for all app features that can be navigated to.
 *
 * Features are added to the navigation graph by calling [feature] within the
 * [NavHost]'s graph builder callback. Feature implementations may choose to
 * provide builder methods that take additional input arguments.
 */
public interface NavFeature : NavScreen {

    /**
     * Adds this feature to the navigation graph.
     *
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
        enter: NavEnterSpec? = null,
        exit: NavExitSpec? = null,
        popEnter: NavEnterSpec? = enter,
        popExit: NavExitSpec? = exit,
    )
}

/**
 * Supertype for [NavFeature] implementations that add a single composable to
 * the navigation graph.
 *
 * @param path The unique path of the node's route string.
 * @param arguments The list of arguments supported by the node.
 * @param deepLinks The list of deep links associated with the node.
 * @param enter Callback defining the default `enter` transition for this node.
 * @param exit Callback defining the default `exit` transition for this node.
 * @param popEnter Callback defining the default `popEnter` transition for this node.
 * @param popExit Callback defining the default `popExit` transition for this node.
 */
public abstract class ComposableFeature(
    path: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enter: NavEnterSpec? = null,
    exit: NavExitSpec? = null,
    popEnter: NavEnterSpec? = enter,
    popExit: NavExitSpec? = exit,
) : NavFeature,
    ComposableScreen(path, arguments, deepLinks, enter, exit, popEnter, popExit) {

    override fun NavGraphBuilder.feature(
        enter: NavEnterSpec?,
        exit: NavExitSpec?,
        popEnter: NavEnterSpec?,
        popExit: NavExitSpec?
    ) {
        composable(
            screen = this@ComposableFeature,
            enter = enter,
            exit = exit,
            popEnter = popEnter,
            popExit = popExit,
            content = { entry -> Content(entry) }
        )
    }

    /**
     * The feature's composable content added to the navigation graph.
     */
    @Composable
    public abstract fun AnimatedContentScope.Content(backStackEntry: NavBackStackEntry)
}

/**
 * Supertype for [NavFeature] implementations that add a nested graph into the
 * parent navigation graph.
 *
 * @param path The unique path of the node's route string.
 * @param startNode The nested graphs start destination.
 * @param arguments The list of arguments supported by the node.
 * @param deepLinks The list of deep links associated with the node.
 * @param enter Callback defining the default `enter` transition for destinations
 * in the nested graph.
 * @param exit Callback defining the default `exit` transition for destinations
 * in the nested graph.
 * @param popEnter Callback defining the default `popEnter` transition for destinations
 * in the nested graph.
 * @param popExit Callback defining the default `popExit` transition for destinations
 * in the nested graph.
 */
public abstract class NestedGraphFeature(
    path: String,
    startNode: NavScreen,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enter: NavEnterSpec? = null,
    exit: NavExitSpec? = null,
    popEnter: NavEnterSpec? = enter,
    popExit: NavExitSpec? = exit,
) : NavFeature,
    NestedGraphScreen(path, startNode, arguments, deepLinks, enter, exit, popEnter, popExit) {

    override fun NavGraphBuilder.feature(
        enter: NavEnterSpec?,
        exit: NavExitSpec?,
        popEnter: NavEnterSpec?,
        popExit: NavExitSpec?
    ) {
        navigation(
            screen = this@NestedGraphFeature,
            enter = enter,
            exit = exit,
            popEnter = popEnter,
            popExit = popExit,
            builder = { content() },
        )
    }

    /**
     * The builder for the nested graph that is added to the parent navigation
     * graph.
     */
    protected abstract fun NavGraphBuilder.content()
}
