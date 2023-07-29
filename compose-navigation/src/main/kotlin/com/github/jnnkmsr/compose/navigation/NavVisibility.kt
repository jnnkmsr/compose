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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Composition local providing the default [EnterTransition] within the scope
 * of a [NavHost] composable, as specified using the [NavHost]'s `defaultEnter`
 * parameter.
 *
 * @throws IllegalStateException if read outside the [NavHost].
 */
public val LocalNavEnterTransition: ProvidableCompositionLocal<EnterTransition> =
    compositionLocalOf {
        error("LocalNavEnterTransition called outside of a NavHost")
    }

/**
 * Composition local providing the default [ExitTransition] within the scope
 * of a [NavHost] composable, as specified using the [NavHost]'s `defaultExit`
 * parameter.
 *
 * @throws IllegalStateException if read outside the [NavHost].
 */
public val LocalNavExitTransition: ProvidableCompositionLocal<ExitTransition> =
    compositionLocalOf {
        error("LocalNavExitTransition called outside of a NavHost")
    }

/**
 * Delegate to [AnimatedVisibility], using the default [enter]/[exit] transitions
 * provided by [LocalNavExitTransition] and [LocalNavExitTransition] within the
 * scope of the [NavHost] composable.
 *
 * @receiver The [Transition] returning the visibility state.
 * @param modifier The [Modifier] to be applied to the [AnimatedVisibility]
 *   container.
 * @param enter [EnterTransition] used for the appearing animation. Defaults to
 *   the [LocalNavEnterTransition], providing the `defaultEnter` transition
 *   passed to the [NavHost].
 * @param exit [ExitTransition] used for the appearing animation. Defaults to
 *   the [LocalNavExitTransition], providing the `defaultExit` transition
 *   passed to the [NavHost].
 * @param content The animated content depending on the [transition]'s
 *   visibility state.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun Transition<Boolean>.NavVisibility(
    modifier: Modifier = Modifier,
    enter: EnterTransition = LocalNavEnterTransition.current,
    exit: ExitTransition = LocalNavExitTransition.current,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = { it },
        modifier = modifier,
        enter = enter,
        exit = exit,
        content = content,
    )
}

/**
 * Delegate to [AnimatedVisibility], using the default [enter]/[exit] transitions
 * provided by [LocalNavExitTransition] and [LocalNavExitTransition] within the
 * scope of the [NavHost] composable.
 *
 * @param visible Callback returning content's visibility state.
 * @param modifier The [Modifier] to be applied to the [AnimatedVisibility]
 *   container.
 * @param enter [EnterTransition] used for the appearing animation. Defaults to
 *   the [LocalNavEnterTransition], providing the `defaultEnter` transition
 *   passed to the [NavHost].
 * @param exit [ExitTransition] used for the appearing animation. Defaults to
 *   the [LocalNavExitTransition], providing the `defaultExit` transition
 *   passed to the [NavHost].
 * @param exit Callback defining thw `exit` transition. Defaults to the value
 *   of [LocalNavExitTransition], providing the `defaultExit` transition
 *   passed to the [NavHost].
 * @param label A label to differentiate from other animations in Android Studio.
 * @param content The animated content depending on the [transition]'s
 *   visibility state.
 */
@Composable
public fun NavVisibility(
    visible: () -> Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = LocalNavEnterTransition.current,
    exit: ExitTransition = LocalNavExitTransition.current,
    label: String = "NavVisibility",
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible(),
        modifier = modifier,
        enter = enter,
        exit = exit,
        label = label,
        content = content,
    )
}

/**
 * Delegate to [AnimatedContent], using a default [ContentTransform] provided by
 * [LocalNavExitTransition] and [LocalNavExitTransition] within the scope of the
 * [NavHost] composable.
 *
 * @receiver The [Transition] for animating between content.
 * @param modifier The [Modifier] to be applied to the [AnimatedContent]
 *   container.
 * @param enter [EnterTransition] used for the appearing animation. Defaults to
 *   the [LocalNavEnterTransition], providing the `defaultEnter` transition
 *   passed to the [NavHost].
 * @param exit [ExitTransition] used for the appearing animation. Defaults to
 *   the [LocalNavExitTransition], providing the `defaultExit` transition
 *   passed to the [NavHost].
 * @param contentAlignment An [Alignment] for the content container.
 * @param contentKey A unique key identifying the layout corresponding to each
 *   [targetState][Transition.targetState] value. Defaults to the
 *   [targetState][Transition.targetState] value.
 * @param content The animated content depending on the
 *   [targetState][Transition.targetState] of the [Transition].
 */
@Composable
public fun <StateT> Transition<StateT>.NavContent(
    modifier: Modifier = Modifier,
    enter: EnterTransition = LocalNavEnterTransition.current,
    exit: ExitTransition = LocalNavExitTransition.current,
    contentAlignment: Alignment = Alignment.TopStart,
    contentKey: (targetState: StateT) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: StateT) -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        transitionSpec = { enter togetherWith exit },
        contentAlignment = contentAlignment,
        contentKey = contentKey,
        content = content,
    )
}

/**
 * Delegate to [AnimatedContent].
 *
 * @receiver The [Transition] for animating between content.
 * @param transitionSpec Callback returning a [ContentTransform].
 * @param modifier The [Modifier] to be applied to the [AnimatedContent]
 *   container.
 * @param contentAlignment An [Alignment] for the content container.
 * @param contentKey A unique key identifying the layout corresponding to each
 *   [targetState][Transition.targetState] value. Defaults to the
 *   [targetState][Transition.targetState] value.
 * @param content The animated content depending on the
 *   [targetState][Transition.targetState] of the [Transition].
 */
@Composable
public fun <StateT> Transition<StateT>.NavContent(
    transitionSpec: AnimatedContentTransitionScope<StateT>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    contentKey: (targetState: StateT) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: StateT) -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        transitionSpec = transitionSpec,
        contentAlignment = contentAlignment,
        contentKey = contentKey,
        content = content,
    )
}

/**
 * Delegate to [AnimatedContent], using a default [ContentTransform] provided by
 * [LocalNavExitTransition] and [LocalNavExitTransition] within the scope of the
 * [NavHost] composable.
 *
 * @param targetState Callback returning the target state.
 * @param modifier The [Modifier] to be applied to the [AnimatedContent]
 *   container.
 * @param enter [EnterTransition] used for the appearing animation. Defaults to
 *   the [LocalNavEnterTransition], providing the `defaultEnter` transition
 *   passed to the [NavHost].
 * @param exit [ExitTransition] used for the appearing animation. Defaults to
 *   the [LocalNavExitTransition], providing the `defaultExit` transition
 *   passed to the [NavHost].
 * @param contentAlignment An [Alignment] for the content container.
 * @param label A label to differentiate from other animations in Android Studio.
 * @param contentKey A unique key identifying the layout corresponding to each
 *   [targetState] value. Defaults to the [targetState] value.
 * @param content The animated content depending on the [targetState].
 */
@Composable
public fun <StateT> NavContent(
    targetState: () -> StateT,
    modifier: Modifier = Modifier,
    enter: EnterTransition = LocalNavEnterTransition.current,
    exit: ExitTransition = LocalNavExitTransition.current,
    contentAlignment: Alignment = Alignment.TopStart,
    label: String = "NavContent",
    contentKey: (targetState: StateT) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: StateT) -> Unit,
) {
    AnimatedContent(
        targetState = targetState(),
        modifier = modifier,
        transitionSpec = { enter togetherWith exit },
        contentAlignment = contentAlignment,
        label = label,
        contentKey = contentKey,
        content = content,
    )
}

/**
 * Delegate to [AnimatedContent].
 *
 * @param targetState Callback returning the target state.
 * @param transitionSpec Callback returning a [ContentTransform].
 * @param modifier The [Modifier] to be applied to the [AnimatedContent]
 *   container.
 * @param contentAlignment An [Alignment] for the content container.
 * @param label A label to differentiate from other animations in Android Studio.
 * @param contentKey A unique key identifying the layout corresponding to each
 *   [targetState] value. Defaults to the [targetState] value.
 * @param content The animated content depending on the [targetState].
 */
@Composable
public fun <StateT> NavContent(
    targetState: () -> StateT,
    transitionSpec: AnimatedContentTransitionScope<StateT>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    label: String = "NavContent",
    contentKey: (targetState: StateT) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: StateT) -> Unit,
) {
    AnimatedContent(
        targetState = targetState(),
        modifier = modifier,
        transitionSpec = transitionSpec,
        contentAlignment = contentAlignment,
        label = label,
        contentKey = contentKey,
        content = content,
    )
}
