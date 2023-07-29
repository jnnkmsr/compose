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

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.get

/**
 * Supertype for all app features that can be navigated to.
 *
 * Features are added to the navigation graph by calling [feature] within the
 * [NavHost]'s graph builder callback. Feature implementations may choose to
 * provide builder methods that take additional input arguments.
 */
public interface NavDialog : NavNode {

    /**
     * The default [EnterTransition] for this dialog. If this value is `null`,
     * the default transition specified for the [DialogHost] will be used
     * instead.
     */
    public val enter: EnterTransition?

    /**
     * The default [ExitTransition] for this dialog. If this value is `null`,
     * the default transition specified for the [DialogHost] will be used
     * instead.
     */
    public val exit: ExitTransition?

    /**
     * Adds this dialog to the navigation graph.
     *
     * @param enter The [EnterTransition] for this dialog. If no non-null value
     *   is provided, the default [enter][NavDialog.enter] is used instead.
     * @param exit The [ExitTransition] for this dialog. If no non-null value
     *   is provided, the default [exit][NavDialog.exit] is used instead.
     */
    public fun NavGraphBuilder.dialog(
        enter: EnterTransition? = null,
        exit: ExitTransition? = null,
    )
}

/**
 * Supertype for [NavDialog] implementations that add a dialog to [DialogHost].
 *
 * @param path The unique path of the node's route string.
 * @param arguments The list of arguments supported by the node.
 * @param deepLinks The list of deep links associated with the node.
 * @param properties The [DialogProperties] customizing this dialog.
 * @param enter The default [EnterTransition] for this dialog. If no non-`null`
 *   value is specified, the default transition passed to the [DialogHost] will
 *   be used instead.
 * @param exit The default [ExitTransition] for this dialog. If no non-`null`
 *   value is specified, the default transition passed to the [DialogHost] will
 *   be used instead.
 */
public abstract class ComposableDialog(
    path: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    private val properties: DialogProperties = DialogProperties(
        decorFitsSystemWindows = false,
        dismissOnClickOutside = true,
        dismissOnBackPress = true,
        usePlatformDefaultWidth = false,
        securePolicy = SecureFlagPolicy.Inherit,
    ),
    override val enter: EnterTransition? = null,
    override val exit: ExitTransition? = null,
) : NavDialog,
    BaseNavNode(path, arguments, deepLinks) {

    override fun NavGraphBuilder.dialog(
        enter: EnterTransition?,
        exit: ExitTransition?,
    ) {
        addDestination(
            DialogDestination(
                navigator = provider[DialogNavigator::class],
                properties = properties,
                enter = enter ?: this@ComposableDialog.enter,
                exit = exit ?: this@ComposableDialog.exit,
                content = { entry -> Content(entry) },
            ).apply {
                route = this@ComposableDialog.route
                this@ComposableDialog.arguments.forEach { (name, arg) -> addArgument(name, arg) }
                this@ComposableDialog.deepLinks.forEach { deepLink -> addDeepLink(deepLink) }
            }
        )
    }

    /** The dialog's composable content. */
    @Composable
    protected abstract fun AnimatedVisibilityScope.Content(backStackEntry: NavBackStackEntry)
}
