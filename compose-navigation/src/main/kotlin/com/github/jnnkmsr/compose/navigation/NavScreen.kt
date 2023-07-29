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

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.get

/** Supertype for screens that can be navigated to. */
public sealed interface NavScreen : NavNode {

    /**
     * Callback defining the default `enter` transition for this node.
     */
    public val enter: NavEnterSpec?

    /**
     * Callback defining the default `exit` transition for this node.
     */
    public val exit: NavExitSpec?

    /**
     * Callback defining the default `popEnter` transition for this node.
     */
    public val popEnter: NavEnterSpec?

    /**
     * Callback defining the default `popExit` transition for this node.
     */
    public val popExit: NavExitSpec?
}

/**
 * Base implementation for all [NavScreen] types.
 */
public sealed class BaseNavScreen protected constructor(
    /**
     * The unique path of the node's route string.
     */
    path: String,
    override val arguments: List<NamedNavArgument>,
    override val deepLinks: List<NavDeepLink>,
    override val enter: NavEnterSpec?,
    override val exit: NavExitSpec?,
    override val popEnter: NavEnterSpec?,
    override val popExit: NavExitSpec?,
) : NavScreen, BaseNavNode(path, arguments, deepLinks)

/**
 * Supertype for [NavScreen]s with composable content.
 *
 * @param path The unique path of the node's route string.
 * @param arguments The list of arguments supported by the node.
 * @param deepLinks The list of deep links associated with the node.
 * @param enter Callback defining the default `enter` transition for this node.
 * @param exit Callback defining the default `exit` transition for this node.
 * @param popEnter Callback defining the default `popEnter` transition for this node.
 * @param popExit Callback defining the default `popExit` transition for this node.
 */
public open class ComposableScreen(
    path: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enter: NavEnterSpec? = null,
    exit: NavExitSpec? = null,
    popEnter: NavEnterSpec? = enter,
    popExit: NavExitSpec? = exit,
) : BaseNavScreen(path, arguments, deepLinks, enter, exit, popEnter, popExit) {

    /**
     * Add this [ComposableScreen] to the [NavGraphBuilder].
     *
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
        enter: NavEnterSpec? = null,
        exit: NavExitSpec? = null,
        popEnter: NavEnterSpec? = enter,
        popExit: NavExitSpec? = exit,
        content: NavContent,
    ) {
        addDestination(
            ComposeDestination(
                navigator = provider[ComposeNavigator::class],
                content = content,
            ).apply {
                route = this@ComposableScreen.route
                this@ComposableScreen.arguments.forEach { (name, arg) -> addArgument(name, arg) }
                this@ComposableScreen.deepLinks.forEach { deepLink -> addDeepLink(deepLink) }
                (enter ?: this@ComposableScreen.enter)?.let { this.enter = it }
                (exit ?: this@ComposableScreen.exit)?.let { this.exit = it }
                (popEnter ?: this@ComposableScreen.popEnter)?.let { this.popEnter = it }
                (popExit ?: this@ComposableScreen.popExit)?.let { this.popExit = it }
            }
        )
    }
}

/**
 * Supertype for [NavScreen]s holding a nested navigation graph.
 *
 * @param path The unique path of the node's route string.
 * @param startNode The nested graphs start destination.
 * @param arguments The list of arguments supported by the node.
 * @param deepLinks The list of deep links associated with the node.
 * @param enter Callback defining the default `enter` transition for this node.
 * @param exit Callback defining the default `exit` transition for this node.
 * @param popEnter Callback defining the default `popEnter` transition for this node.
 * @param popExit Callback defining the default `popExit` transition for this node.
 */
public open class NestedGraphScreen(
    path: String,
    private val startNode: NavScreen,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enter: NavEnterSpec? = null,
    exit: NavExitSpec? = null,
    popEnter: NavEnterSpec? = enter,
    popExit: NavExitSpec? = exit,
) : BaseNavScreen(path, arguments, deepLinks, enter, exit, popEnter, popExit) {

    /**
     * Constructs a nested [NavGraph] for this [NestedGraphScreen] and adds it
     * to the [NavGraphBuilder].
     *
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
        enter: NavEnterSpec? = null,
        exit: NavExitSpec? = null,
        popEnter: NavEnterSpec? = enter,
        popExit: NavExitSpec? = exit,
        builder: NavGraphBuilder.() -> Unit,
    ) {
        addDestination(
            NavGraphBuilder(
                provider = provider,
                startDestination = startNode.destination(),
                route = this@NestedGraphScreen.route
            )
                .apply(builder)
                .build()
                .apply {
                    this@NestedGraphScreen.arguments.forEach { (name, arg) -> addArgument(name, arg) }
                    this@NestedGraphScreen.deepLinks.forEach { deepLink -> addDeepLink(deepLink) }
                    if (this is ComposeNavGraph) {
                        (enter ?: this@NestedGraphScreen.enter)?.let { this.enter = it }
                        (exit ?: this@NestedGraphScreen.exit)?.let { this.exit = it }
                        (popEnter ?: this@NestedGraphScreen.popEnter)?.let { this.popEnter = it }
                        (popExit ?: this@NestedGraphScreen.popExit)?.let { this.popExit = it }
                    }
                }
        )
    }
}
