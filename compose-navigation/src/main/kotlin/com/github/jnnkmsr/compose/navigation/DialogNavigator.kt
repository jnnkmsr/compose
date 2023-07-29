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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.NavigatorState
import androidx.navigation.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [Navigator] that navigates through [Composable]s hosted within a [Dialog].
 */
@Navigator.Name(DialogNavigator.NAME)
internal class DialogNavigator : Navigator<DialogDestination>() {

    /**
     * Returns the [backStack][NavigatorState.backStack] from the [state].
     */
    internal val backStack: StateFlow<List<NavBackStackEntry>>
        get() = state.backStack

    /**
     * Holds the entry that has been requested to be dismissed until all
     * transitions have been finished, and the dialog is finally being ready to
     * be actually dismissed.
     */
    internal val currentDismissEntry = MutableStateFlow<NavBackStackEntry?>(null)

    /**
     * Indicates whether state should be saved when dismissing the
     * [currentDismissEntry]. Set when calling [onDismissRequest] and read in
     * [onDismissTransitionComplete].
     */
    private var saveStateOnDismiss: Boolean = false

    /**
     * Marks the given [entry] to be dismissed by emitting it as the
     * [currentDismissEntry].
     */
    internal fun onDismissRequest(entry: NavBackStackEntry, saveState: Boolean) {
        saveStateOnDismiss = saveState
        currentDismissEntry.value = entry
    }

    /**
     * To be called by the [DialogHost] once the exit transition of the dialog
     * content has completed and the [Dialog] composable is ready to be
     * dismissed.
     */
    internal fun onDismissTransitionComplete(entry: NavBackStackEntry) {
        if (currentDismissEntry.value == entry) {
            currentDismissEntry.value = null
        }
        state.popWithTransition(entry, saveStateOnDismiss)
    }

    /**
     * To be called by the [DialogHost] when the [Dialog] composable associated
     * with the given [entry] has been dismissed.
     */
    internal fun onDismissed(entry: NavBackStackEntry) =
        state.markTransitionComplete(entry)

    override fun createDestination() = DialogDestination(this)

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?,
    ) = entries.forEach { state.pushWithTransition(it) }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) =
        onDismissRequest(popUpTo, savedState)

    internal companion object {
        /**
         * The unique name of a [DialogNavigator] registered with a
         * [NavigatorProvider].
         */
        internal const val NAME = "dialog"
    }
}

/**
 * Returns the [DialogNavigator] registered with the [NavigatorProvider] or
 * `null` if none is registered.
 */
@Suppress("SwallowedException")
internal val NavController.dialogNavigator: DialogNavigator?
    get() = try {
        navigatorProvider
            .get<Navigator<out NavDestination>>(DialogNavigator.NAME)
            as? DialogNavigator
    } catch (exception: IllegalStateException) {
        null
    }
