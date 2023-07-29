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

import androidx.annotation.RestrictTo
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.github.jnnkmsr.compose.navigation.DefaultNavDispatcher.Command.Navigate
import com.github.jnnkmsr.compose.navigation.DefaultNavDispatcher.Command.NavigateBack
import com.github.jnnkmsr.compose.navigation.DefaultNavDispatcher.Command.NavigateUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Returns a new [NavDispatcher] instance.
 *
 * @param coroutineScope The [CoroutineScope] used for navigating. This scope
 *   __must be running on the main thread__, using [Dispatchers.Main].
 */
public fun NavDispatcher(
    coroutineScope: CoroutineScope,
): NavDispatcher = DefaultNavDispatcher(coroutineScope)

/**
 * Manages app navigation within a [NavHost].
 *
 * An instance will generally be obtained within the apps main activity and
 * passed into the [NavHost]. This instance will then handle navigation events
 * of all app features in the navigation graph.
 */
@Stable
public interface NavDispatcher {

    /**
     * A [Flow] emitting the current [NavDestination] of the [NavHost]. New
     * values are emitted as soon as the navigation transition has completed.
     *
     * @see targetDestination
     */
    public val currentDestination: Flow<NavDestination>

    /**
     * A [Flow] emitting the target [NavDestination] of the [NavHost]. New
     * values are emitted as soon as the navigation transition starts.
     *
     * @see currentDestination
     */
    public val targetDestination: Flow<NavDestination>

    /**
     * Returns a [Flow] emitting a [NavNode] of the given [scope] whenever the
     * [currentDestination]'s [hierarchy] contains a [NavDestination] with a
     * matching route.
     */
    public fun currentNode(scope: Collection<NavNode>): Flow<NavNode>

    /**
     * Returns a [Flow] emitting a [NavNode] of the given [scope] whenever the
     * [targetDestination]'s [hierarchy] contains a [NavDestination] with a
     * matching route.
     */
    public fun targetNode(scope: Collection<NavNode>): Flow<NavNode>

    /**
     * Navigates to a node in the current graph. If an invalid [destination] is
     * given, an [IllegalArgumentException] will be thrown.
     *
     * @param destination The route of the destination.
     * @param options Builder for navigation options.
     *
     * @throws IllegalArgumentException if the given [destination] is invalid.
     */
    public fun navigate(
        destination: String,
        options: (NavOptionsBuilder.(NavController) -> Unit)? = null,
    )

    /**
     * Attempts to navigate up in the navigation hierarchy. Suitable for when
     * the user presses the "Up" button marked with a left (or start)-facing
     * arrow in the upper left (or starting) corner of the app UI.
     *
     * *Up* navigation differs from [*back*][navigateBack] navigation when the
     * user did not reach the current destination from the application's own
     * task (e.g., via a deep link from another app's task). In this case, the
     * current activity (determined by the context used to create the
     * [NavController]) will be finished and the user will be taken to an
     * appropriate destination in this app on its own task.
     */
    public fun navigateUp()

    /**
     * Attempts to pop the navigation back stack, analogous to when the user
     * presses the system back button and the associated [NavHost] has focus.
     */
    public fun navigateBack()

    /**
     * Attaches the given [navController] that is associated with the [NavHost].
     * Any navigation command invoked will be forwarded to the [navController].
     *
     * Make sure to call [onDetach] as soon as the given [navController] becomes
     * detached (usually, when a remembered instance leaves composition).
     *
     * @param navController The [NavController] to be attached.
     * @see onDetach
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public fun onAttach(navController: NavController)

    /**
     * Called when `this` dispatcher is detached from any previously attached
     * [NavController].
     *
     * @see onAttach
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public fun onDetach()
}

private class DefaultNavDispatcher(
    /**
     * The [CoroutineScope] used for navigating, which must be running on
     * [Dispatchers.Main].
     */
    private val coroutineScope: CoroutineScope,
) : NavDispatcher {

    override val currentDestination
        get() = _currentDestination.asSharedFlow()

    private val _currentDestination = MutableSharedFlow<NavDestination>(replay = 1)

    override val targetDestination
        get() = _targetDestination.asSharedFlow()

    private val _targetDestination = MutableSharedFlow<NavDestination>(replay = 1)

    override fun currentNode(scope: Collection<NavNode>) =
        _currentDestination.mapNotNull { destination ->
            scope.firstInHierarchyOrNull(destination)
        }

    override fun targetNode(scope: Collection<NavNode>) =
        _targetDestination.mapNotNull { destination ->
            scope.firstInHierarchyOrNull(destination)
        }

    /**
     * A rendezvous channel of navigation [Command]s that are sent by calling
     * [navigate] or [navigateUp] and collected and invoked by an attached
     * [NavController].
     */
    private val commandChannel = Channel<Command>()

    /** Helper function for sending the given [command] to the [commandChannel]. */
    private fun sendCommand(command: Command) {
        coroutineScope.launch { commandChannel.send(command) }
    }

    override fun navigate(
        destination: String,
        options: (NavOptionsBuilder.(NavController) -> Unit)?,
    ) = sendCommand(Navigate(destination, options))

    override fun navigateUp() = sendCommand(NavigateUp)

    override fun navigateBack() = sendCommand(NavigateBack)

    /**
     * Set to the [Command] collection and invocation [Job] that is launched
     * [attaching][onAttach] a [NavController].
     */
    private var navControllerAttachment: Job? = null

    /**
     * Attaches the given [navController] that is associated with the [NavHost]
     * and invokes navigation calls of this dispatcher.
     */
    override fun onAttach(navController: NavController) {
        coroutineScope.launch {
            navControllerAttachment?.cancelAndJoin()
            navControllerAttachment = launch {
                launch {
                    // Collect with receiveAsFlow() instead of consumeAsFlow() to
                    // avoid canceling the Channel when collection is interrupted.
                    commandChannel.receiveAsFlow()
                        .collect { command -> command(navController) }
                }
                launch {
                    // Collect the latest back stack entry as soon as it changes
                    // and emit it as the new target entry.
                    navController.currentBackStackEntryFlow.collect { entry ->
                        _targetDestination.emit(entry.destination)

                        // Suspend until the transition has finished before emitting
                        // to currentDestination.
                        navController.composeNavigator?.isTransitionInProgress?.first { !it }
                        _currentDestination.emit(entry.destination)
                    }
                }
            }
        }
    }

    /**
     * Cancels all [Job]s related to previously attached [NavController].
     */
    override fun onDetach() {
        coroutineScope.launch {
            navControllerAttachment?.cancelAndJoin()
            navControllerAttachment = null
        }
    }

    /**
     * Navigation commands are sent by calling the dispatcher's [navigate] or
     * [navigateUp] functions and then collected and [invoked][invoke] by the
     * attached [NavController].
     */
    private sealed interface Command {

        /**
         * Invokes the navigation action with the given [navController].
         */
        public operator fun invoke(navController: NavController)

        /**
         * A [Command] that navigates the given [destination] when invoked.
         *
         * @param destination The route of the destination.
         * @param options Builder for navigation options.
         */
        public class Navigate(
            private val destination: String,
            private val options: (NavOptionsBuilder.(NavController) -> Unit)?,
        ) : Command {
            override fun invoke(navController: NavController) =
                navController.navigate(
                    route = destination,
                    navOptions = options?.let { navOptions { it(navController) } },
                )
        }

        /**
         * A [Command] that attempts to navigate up in the navigation hierarchy
         * when invoked.
         */
        public data object NavigateUp : Command {
            override fun invoke(navController: NavController) {
                navController.navigateUp()
            }
        }

        /**
         * A [Command] that attempts to pop the navigation back stack.
         */
        public data object NavigateBack : Command {
            override fun invoke(navController: NavController) {
                navController.popBackStack()
            }
        }
    }
}

/**
 * Helper function returning the first [NavNode] in `this` [Collection] whose
 * [route][NavNode.route] matches any of the [routes][NavDestination.route] in
 * the given [destination]'s [hierarchy], or `null` if there is no match.
 */
private fun Collection<NavNode>.firstInHierarchyOrNull(
    destination: NavDestination,
): NavNode? = destination.hierarchy.firstNotNullOfOrNull { parentDestination ->
    this.firstOrNull { node -> node.route == parentDestination.route }
}
