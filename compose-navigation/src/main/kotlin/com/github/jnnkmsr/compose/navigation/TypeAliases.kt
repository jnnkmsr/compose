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
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination

/** Composable content of [ComposeDestination]s. */
public typealias NavContent =
    @Composable
    AnimatedContentScope.(NavBackStackEntry) -> Unit

/** Composable content of [DialogDestination]s. */
public typealias NavDialogContent =
    @Composable
    AnimatedVisibilityScope.(NavBackStackEntry) -> Unit

/** Enter transition spec for composable [NavDestination]s. */
public typealias NavEnterSpec =
    AnimatedContentTransitionScope<@JvmSuppressWildcards NavBackStackEntry>.() -> EnterTransition

/** Exit transition spec for composable [NavDestination]s. */
public typealias NavExitSpec =
    AnimatedContentTransitionScope<@JvmSuppressWildcards NavBackStackEntry>.() -> ExitTransition

/**
 * Enter transition spec for composable navigation destinations in [NavContent]
 * functions.
 */
public typealias NavContentEnterSpec =
    AnimatedContentTransitionScope<@JvmSuppressWildcards String?>.() -> EnterTransition

/**
 * Exit transition spec for composable navigation destinations in [NavContent]
 * functions.
 */
public typealias NavContentExitSpec =
    AnimatedContentTransitionScope<@JvmSuppressWildcards String?>.() -> ExitTransition
