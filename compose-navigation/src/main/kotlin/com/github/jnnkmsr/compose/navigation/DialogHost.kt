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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavigatorProvider
import androidx.navigation.compose.LocalOwnersProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * Navigation host for [DialogDestination]s. Shows each destination on the
 * [DialogNavigator]'s back stack as a [Dialog].
 *
 * [DialogHost] is called within the main [NavHost]; you do not need to call it
 * manually.
 *
 * @param navigator The [DialogNavigator] provided by the [NavigatorProvider].
 * @param enter The default [EnterTransition] for dialogs in this host.
 * @param exit The default [ExitTransition] for dialogs in this host.
 * @param modifier The [Modifier] applied to the [AnimatedVisibility] container
 *   for the dialog content.
 * @param padding The minimum padding between dialogs and the screen edges.
 * @param paneTitle Dialog pane title for accessibility purposes.
 */
@Composable
internal fun DialogHost(
    navigator: DialogNavigator,
    enter: EnterTransition,
    exit: ExitTransition,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(NavDefaults.DialogOuterPadding),
    paneTitle: String = stringResource(R.string.dialog_host_pane_title),
) {
    val backStack by navigator.backStack.collectAsState()
    val visibleEntries = rememberVisibleDialogEntries(backStack)

    val saveableStateHolder = rememberSaveableStateHolder()

    val dismissEntry by navigator.currentDismissEntry.collectAsState()

    visibleEntries.forEach { entry ->
        key(entry.id) {
            val destination = entry.destination as DialogDestination
            val dismissRequested = remember { derivedStateOf { dismissEntry == entry } }

            Dialog(
                dismissRequested = dismissRequested::value,
                onDismissRequest = { navigator.onDismissRequest(entry, false) },
                onDismissTransitionComplete = { navigator.onDismissTransitionComplete(entry) },
                onDismissed = { navigator.onDismissed(entry) },
                modifier = modifier,
                properties = destination.properties,
                enter = destination.enter ?: enter,
                exit = destination.exit ?: exit,
                padding = padding,
                paneTitle = paneTitle,
            ) {
                // While in the scope of the composable, provide the entry as
                // ViewModelStoreOwner and LifecycleOwner.
                entry.LocalOwnersProvider(saveableStateHolder) {
                    destination.content(this, entry)
                }
            }
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun Dialog(
    dismissRequested: () -> Boolean,
    onDismissRequest: () -> Unit,
    onDismissTransitionComplete: () -> Unit,
    onDismissed: () -> Unit,
    modifier: Modifier,
    properties: DialogProperties,
    enter: EnterTransition,
    exit: ExitTransition,
    padding: PaddingValues,
    paneTitle: String,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    val backgroundTapModifier = if (properties.dismissOnClickOutside) {
        Modifier.pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
    } else {
        Modifier
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        val visibleState = remember { MutableTransitionState(false) }

        DialogLayout {
            Box(
                Modifier
                    .layoutId(DialogLayoutSlot.Background)
                    .fillMaxSize()
                    .then(backgroundTapModifier)
            )
            Box(
                Modifier
                    .layoutId(DialogLayoutSlot.Content)
                    .padding(
                        if (properties.usePlatformDefaultWidth) {
                            PaddingValues(
                                top = padding.calculateTopPadding(),
                                bottom = padding.calculateBottomPadding(),
                            )
                        } else {
                            padding
                        }
                    )
            ) {
                AnimatedVisibility(
                    visibleState = visibleState,
                    modifier = modifier
                        .semantics { this.paneTitle = paneTitle }
                        .pointerInput(Unit) {},
                    enter = enter,
                    exit = exit,
                    label = DialogNavigator.NAME,
                    content = content,
                )
            }
        }

        val keyboardController = LocalSoftwareKeyboardController.current
        val latestDismissRequested by rememberUpdatedState(dismissRequested)
        val latestOnDismissTransitionComplete by rememberUpdatedState(onDismissTransitionComplete)

        LaunchedEffect(visibleState) {
            // Start animating the dialog appearance after a short delay. The
            // delay is a workaround for preventing a slide in effect before the
            // dialog size constraints have settled.
            delay(NavDefaults.DialogEnterDelay)
            visibleState.targetState = true

            // Pass dismiss request to the transition state first before
            // actually dismissing the Dialog. Also take care of hiding the
            // software keyboard automatically when a dialog is dismissed.
            launch {
                snapshotFlow { latestDismissRequested() }
                    .filter { it }
                    .collect {
                        keyboardController?.hide()
                        visibleState.targetState = false
                    }
            }

            // Launch onDismissTransitionComplete when the transition has
            // completed. This will take care of actually dismissing the Dialog.
            launch {
                snapshotFlow { visibleState.currentState }
                    .filter { !it && !visibleState.targetState }
                    .collect { latestOnDismissTransitionComplete() }
            }
        }

        // Launch onDismissed when the dialog has left composition.
        DisposableEffect(Unit) { onDispose(onDismissed) }
    }
}

@Composable
private fun DialogLayout(
    modifier: Modifier = Modifier,
    imeInsets: WindowInsets = WindowInsets.ime,
    content: @Composable () -> Unit,
) {
    Layout(content, modifier) { measurables, constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val backgroundPlaceable = measurables
            .firstOrNull { it.layoutId == DialogLayoutSlot.Background }
            ?.measure(Constraints.fixed(layoutWidth, layoutHeight))

        val imePadding = imeInsets.getBottom(this)
        val maxContentHeight = layoutHeight - imePadding

        val contentPlaceable = measurables
            .firstOrNull { it.layoutId == DialogLayoutSlot.Content }
            ?.measure(
                Constraints(
                    minWidth = 0, maxWidth = layoutWidth,
                    minHeight = 0, maxHeight = max(maxContentHeight, 0),
                )
            )

        layout(layoutWidth, layoutHeight) {
            backgroundPlaceable?.place(0, 0)
            contentPlaceable?.run {
                val contentY = (layoutHeight - height) / 2
                val imeShift = max(imePadding - contentY, 0)

                place(
                    x = (layoutWidth - width) / 2,
                    y = contentY - imeShift
                )
            }
        }
    }
}

/** Unique key for [DialogLayout] components. */
@Immutable
private sealed interface DialogLayoutSlot {
    public object Content : DialogLayoutSlot
    public object Background : DialogLayoutSlot
}

@Composable
private fun rememberVisibleDialogEntries(
    backStack: List<NavBackStackEntry>,
    inspectionMode: Boolean = LocalInspectionMode.current,
): SnapshotStateList<NavBackStackEntry> =
    remember(backStack) {
        mutableStateListOf<NavBackStackEntry>().apply {
            addAll(
                backStack.filter { entry ->
                    inspectionMode || entry.lifecycle.currentState.isAtLeast(STARTED)
                }
            )
        }
    }.apply {
        PopulateVisibleDialogEntries(backStack, inspectionMode)
    }

@Composable
private fun MutableList<NavBackStackEntry>.PopulateVisibleDialogEntries(
    backStack: List<NavBackStackEntry>,
    inspectionMode: Boolean = LocalInspectionMode.current,
) = backStack.forEach { entry ->
    DisposableEffect(entry.lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when {
                // Always show dialog in previews.
                inspectionMode && !contains(entry) -> add(entry)
                // Add back stack entry to the list of visible entries when
                // its lifecycle has started.
                event == ON_START && !contains(entry) -> add(entry)
                // Remove back stack entry from the list of visible entries
                // when its lifecycle has stopped.
                event == ON_STOP -> remove(entry)
                else -> Unit
            }
        }
        entry.lifecycle.addObserver(observer)
        onDispose {
            entry.lifecycle.removeObserver(observer)
        }
    }
}
