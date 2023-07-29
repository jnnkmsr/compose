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

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.Navigator

/**
 * Creates and remembers a [NavHostController] across compositions and process
 * recreation and adds all required [Navigator]s.
 */
@Composable
internal fun rememberNavController(): NavHostController {
    val context = LocalContext.current

    return rememberSaveable(
        saver = navControllerSaver(context),
        init = { createNavController(context) },
    )
}

/**
 * Creates a [NavHostController] and provides the required [ComposeNavigator],
 * [ComposeNavGraphNavigator] and [DialogNavigator].
 */
private fun createNavController(context: Context) =
    NavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavGraphNavigator(navigatorProvider))
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }

/**
 * Saver to save and restore [NavHostController] instances across configuration
 * change and process death.
 */
private fun navControllerSaver(
    context: Context,
): Saver<NavHostController, *> = Saver(
    save = { navController -> navController.saveState() },
    restore = { bundle -> createNavController(context).apply { restoreState(bundle) } }
)
