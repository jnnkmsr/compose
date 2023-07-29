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

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.Navigator

/**
 * [NavDestination] displaying [Composable] content.
 *
 * @param navigator The [ComposeNavigator] associated with the destination.
 * @param content The composable content of the destination.
 */
@NavDestination.ClassType(Composable::class)
internal class ComposeDestination(
    navigator: ComposeNavigator,
    internal val content: NavContent = {},
) : NavDestination(navigator) {

    /**
     * Callback defining the `enter` transition for this destination.
     */
    internal var enter: NavEnterSpec? = null

    /**
     * Callback defining the `exit` transition for this destination.
     */
    internal var exit: NavExitSpec? = null

    /**
     * Callback defining the `popEnter` transition for this destination.
     */
    internal var popEnter: NavEnterSpec? = null

    /**
     * Callback defining the `popExit` transition for this destination.
     */
    internal var popExit: NavExitSpec? = null
}

/**
 * [NavGraph] that can be navigated to with animated transitions.
 */
internal class ComposeNavGraph(
    navigator: Navigator<out NavGraph>,
) : NavGraph(navigator) {

    /**
     * Callback defining the default `enter` transition for destinations in
     * this graph.
     */
    internal var enter: NavEnterSpec? = null

    /**
     * Callback defining the default `exit` transition for destinations in
     * this graph.
     */
    internal var exit: NavExitSpec? = null

    /**
     * Callback defining the default `popEnter` transition for destinations in
     * this graph.
     */
    internal var popEnter: NavEnterSpec? = null

    /**
     * Callback defining the default `popExit` transition for destinations in
     * this graph.
     */
    internal var popExit: NavExitSpec? = null
}

/**
 * [NavDestination] specific to [DialogNavigator].
 *
 * @param navigator The [DialogNavigator] associated with the destination.
 */
@NavDestination.ClassType(Composable::class)
internal class DialogDestination(
    navigator: DialogNavigator,
    internal val properties: DialogProperties = DialogProperties(),
    internal val enter: EnterTransition? = null,
    internal val exit: ExitTransition? = null,
    internal val content: NavDialogContent = {},
) : NavDestination(navigator), FloatingWindow

/**
 * Finds the first specified `enter` transition spec in the [targetDestination]'s
 * [hierarchy] and converts it to an [EnterTransition]. If no destination in the
 * hierarchy has a non-null `enter` transition spec, the [fallback] os used
 * instead.
 */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.createEnterTransition(
    targetDestination: NavDestination,
    fallback: NavEnterSpec,
): EnterTransition =
    targetDestination.hierarchy.firstNotNullOfOrNull { destination ->
        when (destination) {
            is ComposeDestination -> destination.enter
            is ComposeNavGraph -> destination.enter
            else -> null
        }?.invoke(this)
    } ?: fallback(this)

/**
 * Finds the first specified `popEnter` transition spec in the [targetDestination]'s
 * [hierarchy] and converts it to an [EnterTransition]. If no destination in the
 * hierarchy has a non-null `popEnter` transition spec, the [fallback] os used
 * instead.
 */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.createPopEnterTransition(
    targetDestination: NavDestination,
    fallback: NavEnterSpec,
): EnterTransition =
    targetDestination.hierarchy.firstNotNullOfOrNull { destination ->
        when (destination) {
            is ComposeDestination -> destination.popEnter
            is ComposeNavGraph -> destination.popEnter
            else -> null
        }?.invoke(this)
    } ?: fallback(this)

/**
 * Finds the first specified `exit` transition spec in the [targetDestination]'s
 * [hierarchy] and converts it to an [ExitTransition]. If no destination in the
 * hierarchy has a non-null `exit` transition spec, the [fallback] os used
 * instead.
 */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.createExitTransition(
    targetDestination: NavDestination,
    fallback: NavExitSpec,
): ExitTransition =
    targetDestination.hierarchy.firstNotNullOfOrNull { destination ->
        when (destination) {
            is ComposeDestination -> destination.exit
            is ComposeNavGraph -> destination.exit
            else -> null
        }?.invoke(this)
    } ?: fallback(this)

/**
 * Finds the first specified `popExit` transition spec in the [targetDestination]'s
 * [hierarchy] and converts it to an [ExitTransition]. If no destination in the
 * hierarchy has a non-null `popExit` transition spec, the [fallback] os used
 * instead.
 */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.createPopExitTransition(
    targetDestination: NavDestination,
    fallback: NavExitSpec,
): ExitTransition =
    targetDestination.hierarchy.firstNotNullOfOrNull { destination ->
        when (destination) {
            is ComposeDestination -> destination.popExit
            is ComposeNavGraph -> destination.popExit
            else -> null
        }?.invoke(this)
    } ?: fallback(this)
