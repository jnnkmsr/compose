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

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.LocalOwnersProvider
import androidx.navigation.createGraph
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Provides in place in the Compose hierarchy for self-contained navigation to
 * occur.
 *
 * In the [content] block, the actual composable content provided as a lambda
 * must be called to add the navigation graph into composition. Once this is
 * called, any Composable within the given [graph] can be navigated to from
 * the provided [dispatcher].
 *
 * @param dispatcher The [NavDispatcher] managing navigation for this host.
 * @param startNode The node providing the start destination.
 * @param modifier The modifier to be applied to the layout.
 * @param defaultEnter The [EnterTransition] used for all transitions within
 *   this host by default.
 * @param defaultExit The [ExitTransition] used for all transitions within this
 *   host by default.
 * @param enter Callback defining the default `enter` transition for
 *   destinations in this host. Defaults to [defaultEnter].
 * @param exit Callback defining the default `exit` transition for
 *   destinations in this host. Defaults to [defaultExit].
 * @param popEnter Callback defining the default `popEnter` transition for
 *   destinations in this host. Defaults to [enter].
 * @param popExit Callback defining the default `popExit` transition for
 *   destinations in this host. Defaults to [exit].
 * @param dialogEnter The default [EnterTransition] for dialog destinations.
 * @param dialogExit The default [ExitTransition] for dialog destinations.
 * @param contentAlignment The [Alignment] of the [AnimatedContent].
 * @param dialogPadding The minimum padding between dialogs and the screen edges.
 * @param dialogPaneTitle Dialog pane title for accessibility purposes.
 * @param graph The builder used to construct the navigation graph that is
 *   added into composition when calling the provided lambda within the
 *   [content] block.
 * @param content The composable content. The navigation graph content is
 *   provided as a composable lambda, such that it can be wrapped inside,
 *   *e.g.*, a `Scaffold` composables.
 */
@Composable
public fun NavHost(
    dispatcher: NavDispatcher,
    startNode: NavScreen,
    modifier: Modifier = Modifier,
    node: NavScreen? = null,
    defaultEnter: EnterTransition = NavDefaults.Enter,
    defaultExit: ExitTransition = NavDefaults.Exit,
    enter: NavEnterSpec = { defaultEnter },
    exit: NavExitSpec = { defaultExit },
    popEnter: NavEnterSpec = enter,
    popExit: NavExitSpec = exit,
    dialogEnter: EnterTransition = NavDefaults.DialogEnter,
    dialogExit: ExitTransition = NavDefaults.DialogExit,
    contentAlignment: Alignment = Alignment.Center,
    dialogPadding: PaddingValues = PaddingValues(NavDefaults.DialogOuterPadding),
    dialogPaneTitle: String = stringResource(R.string.dialog_host_pane_title),
    graph: NavGraphBuilder.() -> Unit,
    content: @Composable (graph: @Composable () -> Unit) -> Unit,
) {
    NavHost(
        navController = rememberNavController().apply {
            SetBackHandler(dispatcher::navigateBack)
            SetLocalOwners()

            this.graph = remember(node, startNode, graph) {
                createGraph(
                    route = node?.route,
                    startDestination = startNode.destination(),
                    builder = graph,
                )
            }

            DisposableEffect(dispatcher, this) {
                dispatcher.onAttach(this@apply)
                onDispose { dispatcher.onDetach() }
            }
        },
        modifier = modifier,
        defaultEnter = defaultEnter,
        defaultExit = defaultExit,
        enter = enter,
        exit = exit,
        popEnter = popEnter,
        popExit = popExit,
        dialogEnter = dialogEnter,
        dialogExit = dialogExit,
        contentAlignment = contentAlignment,
        dialogPadding = dialogPadding,
        dialogPaneTitle = dialogPaneTitle,
        content = content,
    )
}

/**
 * Provides in place in the Compose hierarchy for self-contained navigation to
 * occur.
 *
 * @param navController The [NavHostController] holding the [NavGraph] for this
 * host.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param defaultEnter The [EnterTransition] used for all transitions within
 *   this host by default.
 * @param defaultExit The [ExitTransition] used for all transitions within this
 *   host by default.
 * @param enter Callback defining the default `enter` transition for
 *   destinations in this host.
 * @param exit Callback defining the default `exit` transition for
 *   destinations in this host.
 * @param popEnter Callback defining the default `popEnter` transition for
 *   destinations in this host.
 * @param popExit Callback defining the default `popExit` transition for
 *   destinations in this host.
 * @param dialogEnter The default [EnterTransition] for dialog destinations.
 * @param dialogExit The default [ExitTransition] for dialog destinations.
 * @param contentAlignment The [Alignment] of the [AnimatedContent].
 * @param dialogPadding The minimum padding between dialogs and the screen edges.
 * @param dialogPaneTitle Dialog pane title for accessibility purposes.
 * @param content The composable content of the host.
 */
@Suppress("LongMethod")
@Composable
private fun NavHost(
    navController: NavHostController,
    modifier: Modifier,
    defaultEnter: EnterTransition,
    defaultExit: ExitTransition,
    enter: NavEnterSpec,
    exit: NavExitSpec,
    popEnter: NavEnterSpec,
    popExit: NavExitSpec,
    dialogEnter: EnterTransition,
    dialogExit: ExitTransition,
    contentAlignment: Alignment,
    dialogPadding: PaddingValues,
    dialogPaneTitle: String,
    content: @Composable (graph: @Composable () -> Unit) -> Unit,
) {
    // Find the ComposeNavigator, returning early if it isn't found (e.g., when
    // using a TestNavHostController).
    val navigator = navController.composeNavigator ?: return

    // Get the list of visible NavBackStackEntries as state. If inspecting
    // (Preview), show only the startDestination.
    val entries by if (LocalInspectionMode.current) {
        navigator.backStack.collectAsState()
    } else {
        remember(navController.visibleEntries) {
            navController.visibleEntries.map { entries ->
                entries.filter { ComposeNavigator.isDestination(it.destination) }
            }
        }.collectAsState(emptyList())
    }
    val entry = entries.lastOrNull()

    // A map of zIndex values by route to correctly stack screens when
    // navigating back and forth.
    val zIndices = remember { mutableMapOf<String, Float>() }

    if (entry != null) {
        CompositionLocalProvider(
            LocalNavEnterTransition provides defaultEnter,
            LocalNavExitTransition provides defaultExit,
        ) {
            NavHost(
                transition = updateTransition(entry, NavHostTransitionLabel),
                enter = {
                    if (navigator.isPop) {
                        createPopEnterTransition(targetState.destination, popEnter)
                    } else {
                        createEnterTransition(targetState.destination, enter)
                    }
                },
                exit = {
                    if (navigator.isPop) {
                        createPopExitTransition(targetState.destination, popExit)
                    } else {
                        createExitTransition(targetState.destination, exit)
                    }
                },
                zIndex = {
                    val initialZIndex = zIndices[initialState.id]
                        ?: 0f.also { zIndices[initialState.id] = it }
                    when {
                        targetState.id == initialState.id -> initialZIndex
                        navigator.isPop -> initialZIndex - 1f
                        else -> initialZIndex + 1f
                    }.also { zIndices[targetState.id] = it }
                },
                onTransitionComplete = {
                    entries.forEach(navigator::onTransitionComplete)
                    zIndices
                        .filter { it.key != targetState.id }
                        .forEach { zIndices.remove(it.key) }
                },
                isVisible = { this in entries },
                modifier = modifier,
                contentAlignment = contentAlignment,
                content = content,
            )
        }
    }

    val dialogNavigator = navController.dialogNavigator ?: return
    DialogHost(
        navigator = dialogNavigator,
        enter = dialogEnter,
        exit = dialogExit,
        padding = dialogPadding,
        paneTitle = dialogPaneTitle,
    )
}

/**
 * Provides in place in the Compose hierarchy for self-contained navigation to
 * occur.
 *
 * @param transition The [Transition] for animating navigation.
 * @param enter Callback returning the `enter` or `popEnter` transition for
 *   destinations in this host.
 * @param exit Callback returning the `exit` or `popExit` transition for
 *   destinations in this host.
 * @param zIndex Callback returning the `zIndex` of the [ContentTransform].
 * @param onTransitionComplete Callback that is executed when the transition's
 *   `currentState` matches the `targetState`.
 * @param isVisible Callback that returns `true` if the receiving
 *   [NavBackStackEntry] is in the list of currently visible entries.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param contentAlignment The [Alignment] of the [AnimatedContent].
 * @param saveableStateHolder The [SaveableStateHolder] instance.
 * @param content The composable content of the host.
 */
@Composable
private fun NavHost(
    transition: Transition<NavBackStackEntry>,
    enter: NavEnterSpec,
    exit: NavExitSpec,
    zIndex: AnimatedContentTransitionScope<NavBackStackEntry>.() -> Float,
    onTransitionComplete: Transition<NavBackStackEntry>.() -> Unit,
    isVisible: NavBackStackEntry.() -> Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    saveableStateHolder: SaveableStateHolder = rememberSaveableStateHolder(),
    content: @Composable (graph: @Composable () -> Unit) -> Unit,
) = content {
    transition.AnimatedContent(
        modifier = modifier,
        transitionSpec = {
            // If the initialState of the AnimatedContent is not in the
            // visibleEntries, the visible state has cleared the old state for
            // some reason. Instead of attempting to animate away from the
            // initialState, we skip the animation.
            if (initialState.isVisible()) {
                ContentTransform(enter(), exit(), zIndex())
            } else {
                EnterTransition.None togetherWith ExitTransition.None
            }
        },
        contentAlignment = contentAlignment,
        contentKey = { entry -> entry.id },
    ) { entry ->
        // In some specific cases (such as clearing the back stack by changing
        // the start destination), AnimatedContent can contain an entry that is
        // no longer part of visibleEntries. In those cases, the currentEntry
        // will be null, and AnimatedContent will just skip attempting to
        // transition the old entry.
        // (See https://issuetracker.google.com/238686802)
        if (entry.isVisible()) {
            entry.LocalOwnersProvider(saveableStateHolder) {
                (entry.destination as ComposeDestination).content(this, entry)
            }
        }
    }

    LaunchedEffect(transition) {
        snapshotFlow { transition.currentState }
            .filter { currentEntry -> currentEntry == transition.targetState }
            .collect { onTransitionComplete(transition) }
    }
}

/**
 * Adds a [BackHandler][androidx.activity.compose.BackHandler] effect that
 * intercepts back only when there is a destination to pop.
 *
 * @param onBack The action invoked by pressing the system back button.
 */
@SuppressLint("RestrictedApi")
@Composable
private fun NavHostController.SetBackHandler(
    onBack: suspend () -> Unit
) {
    val isBackHandlerEnabled by remember(currentBackStack) {
        currentBackStack.map { entries ->
            entries
                .filter { ComposeNavigator.isDestination(it.destination) }
                .size > 1
        }
    }.collectAsState(false)

    val coroutineScope = rememberCoroutineScope()

    BackHandler(isBackHandlerEnabled) {
        coroutineScope.launch { onBack() }
    }
}

/**
 * Sets the host's [LifecycleOwner] and [ViewModelStore]. Must be called before
 * [setGraph][NavHostController.setGraph].
 */
@Composable
private fun NavHostController.SetLocalOwners() {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        setLifecycleOwner(lifecycleOwner)
        onDispose { }
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner provided via LocalViewModelStoreOwner"
    }
    setViewModelStore(viewModelStoreOwner.viewModelStore)
}

/**
 * A label to be used for the [Transition] of the [NavHost] within Android
 * Studio.
 */
private const val NavHostTransitionLabel = "entry"
