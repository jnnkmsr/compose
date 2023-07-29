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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.NavigatorState
import androidx.navigation.get
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest

/**
 * [Navigator] that navigates through [Composable]s. Every destination using
 * this `Navigator` must set a valid [Composable] by setting it directly on an
 * instantiated [ComposeDestination] or calling [composable].
 */
@Navigator.Name(ComposeNavigator.NAME)
internal class ComposeNavigator : Navigator<ComposeDestination>() {

    /**
     * Returns the [backStack][NavigatorState.backStack] from the [state].
     */
    internal val backStack: StateFlow<List<NavBackStackEntry>>
        get() = state.backStack

    /**
     * Returns `true` if the current navigation transition is popping destinations
     * off the back stack, i.e., if the transition is an "Up" or "Back" transition.
     */
    internal var isPop by mutableStateOf(false)
        private set

    private val isStateAttached = MutableStateFlow(false)

    /**
     * Delegates to the [NavigatorState]'s
     * [transitionInProgress][NavigatorState.transitionsInProgress] flow and
     * emits `true` when there are any entries in the set of currently running
     * transitions and `false` if the set is empty.
     *
     * Safe to use even before `this` [Navigator] has been fully attached to
     * the [NavController].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    internal val isTransitionInProgress = isStateAttached
        .transformLatest { isAttached ->
            if (isAttached) {
                emitAll(
                    state
                        .transitionsInProgress
                        .map { entries -> entries.isNotEmpty() }
                )
            } else {
                emit(false)
            }
        }

    override fun createDestination() = ComposeDestination(this)

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        entries.forEach { entry -> state.pushWithTransition(entry) }
        isPop = false
    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        state.popWithTransition(popUpTo, savedState)
        isPop = true
    }

    override fun onAttach(state: NavigatorState) {
        super.onAttach(state)
        isStateAttached.value = true
    }

    /**
     * Callback to mark a navigation in transition as complete.
     *
     * This should be called in conjunction with [navigate] and [popBackStack]
     * as those calls merely start a transition to the target destination, and
     * requires manually marking the transition as complete by calling this
     * method.
     *
     * Failing to call this method could result in entries being prevented from
     * reaching their final [Lifecycle.State].
     */
    internal fun onTransitionComplete(entry: NavBackStackEntry) {
        state.markTransitionComplete(entry)
    }

    /**
     * Extras that can be passed to a [ComposeNavigator] to enable specific
     * behavior such as shared-element transitions.
     *
     * *(To be implemented.)*
     */
    internal class Extras : Navigator.Extras

    internal companion object {
        /**
         * The unique name of a [ComposeNavigator] registered with a
         * [NavigatorProvider].
         */
        internal const val NAME = "composable"

        /**
         * Returns `true` if the given [destination] is associated with a
         * [ComposeNavigator].
         */
        internal fun isDestination(destination: NavDestination) = destination.navigatorName == NAME
    }
}

/**
 * A [NavGraphNavigator] for [ComposeNavGraph] destinations, adding support for
 * defining transitions at the navigation graph level.
 */
@Navigator.Name(ComposeNavGraphNavigator.NAME)
internal class ComposeNavGraphNavigator(
    provider: NavigatorProvider,
) : NavGraphNavigator(provider) {

    override fun createDestination(): NavGraph = ComposeNavGraph(this)

    internal companion object {
        /**
         * The unique name of a [ComposeNavGraphNavigator] registered with a
         * [NavigatorProvider].
         */
        internal const val NAME = "navigation"
    }
}

/**
 * Returns the [ComposeNavigator] registered with the [NavigatorProvider] or
 * `null` if none is registered.
 */
@Suppress("SwallowedException")
internal val NavController.composeNavigator: ComposeNavigator?
    get() = try {
        navigatorProvider
            .get<Navigator<out NavDestination>>(ComposeNavigator.NAME)
            as? ComposeNavigator
    } catch (exception: IllegalStateException) {
        null
    }
