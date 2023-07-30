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

package com.github.jnnkmsr.compose.icon

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.togetherWith
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

/**
 * An icon composable that animates the icon when [targetState] returns a new
 * value.
 *
 * Animations can be provided either by
 * [animated vector-drawable (AVD)](https://d.android.com/develop/ui/views/animations/drawable-animation#AnimVector)
 * resource icons or by [ContentTransform]s returned by the given
 * [transitionSpec].
 *
 * @param targetState Callback returning the target state for animating between
 *   icons.
 * @param icons Callback mapping each state value to a [StaticVectorIcon]. These
 *   icons will only be drawn when not animating.
 * @param contentDescription Callback returning a text used by accessibility
 *   services to describe what the icon represents in a given state. This should
 *   always provide non-`null` values unless the icon is used for decorative
 *   purposes, and does not represent a meaningful action a user can take. The
 *   returned text should be localized, such as be using [stringResource] or
 *   similar.
 * @param modifier The [Modifier] to be applied to the icon container.
 * @param iconTransitionSpec Callback returning an [AnimatedVectorIcon] that will
 *   be used to animate between a given `initialState` and `finalState`. If this
 *   returns `null`, the transition will instead be animated within an
 *   [AnimatedContent] container, using the given [transitionSpec].
 * @param transitionSpec Callback returning a [ContentTransform] for transitions
 *   that are _not_ animated by AVDs. All state transitions with non-`null`
 *   [AnimatedVectorIcon]s returned by [iconTransitionSpec] wil instead by
 *   animated by the AVD.
 * @param size The [IconSize] setting the size of the icon. Defaults to the
 *   current value of [LocalIconSize], which is `null` by default, meaning that
 *   the icon will be drawn at its natural size.
 * @param tint Tint to be applied to the icon. If [Color.Unspecified] is
 *   provided, then no tint is applied. If [tint] is not specified, the current
 *   [LocalContentColor] will be applied.
 */
@Suppress("LongMethod")
@ExperimentalAnimationGraphicsApi
@Composable
public fun <S> AnimatedIcon(
    targetState: () -> S,
    icons: (state: S) -> VectorIcon,
    contentDescription: (state: S) -> String?,
    modifier: Modifier = Modifier,
    iconTransitionSpec: ((initialState: S, finalState: S) -> AnimatedVectorIcon?)? = null,
    transitionSpec: AnimatedContentTransitionScope<S>.() -> ContentTransform =
        { fadeIn() togetherWith fadeOut() },
    size: IconSize? = LocalIconSize.current,
    tint: Color = LocalContentColor.current,
) {
    /**
     * Holds the current state value, which is updated whenever after a
     * transition has completed.
     */
    var currentState by remember { mutableStateOf(targetState()) }

    /**
     * The `targetState` providing lambda, [remember][rememberUpdatedState]ed as
     * an updated state to be referenced by long-lived lambdas.
     */
    val latestTargetState by rememberUpdatedState(targetState)

    /**
     * The callback providing contentDescription strings,
     * [remember][rememberUpdatedState]ed as an updated state to be referenced
     * by long-lived lambdas.
     */
    val latestContentDescription by rememberUpdatedState(contentDescription)

    /**
     * The icon tint, [remember][rememberUpdatedState]ed as an updated state to
     * be referenced by long-lived lambdas.
     */
    val latestTint by rememberUpdatedState(tint)

    /**
     * The icon size, [remember][rememberUpdatedState]ed as an updated state to
     * be referenced by long-lived lambdas.
     */
    val latestSize by rememberUpdatedState(size)

    /**
     * Remembers tracking composable icon content for different state values in
     * a map that is observable by snapshots. Content is added lazily on state
     * changes by calling `getOrPutIconContent`.
     */
    val iconContent = remember(icons) { mutableMapOf<S, @Composable () -> Unit>() }
    val getOrPutIconContent = remember(icons) {
        { state: S ->
            iconContent.getOrPut(state) {
                movableContentOf {
                    Icon(
                        icon = icons(state),
                        contentDescription = latestContentDescription(state),
                        modifier = modifier,
                        tint = latestTint,
                        size = latestSize,
                    )
                }
            }
        }
    }

    /**
     * Remembers the AVD transition composables for all icons returned by
     * [iconTransitionSpec] as a map that is observable by snapshots. Content is
     * added lazily on state changes by calling `getOrPutIconTransition`.
     */
    val animatedIconTransitions = remember(iconTransitionSpec) {
        mutableStateMapOf<Pair<S, S>, @Composable () -> Unit>()
    }
    val getOrPutIconTransition = remember(iconTransitionSpec) {
        { initialState: S, finalState: S ->
            animatedIconTransitions.getOrPutIfNotNull(initialState to finalState) {
                iconTransitionSpec?.invoke(initialState, finalState)?.let { animatedIcon ->

                    /** The static content displaying the icon in its `finalState` */
                    val finalIconContent = getOrPutIconContent(finalState)

                    movableContentOf {
                        /** The target state of the AVD transition. */
                        var targetAtEnd by remember { mutableStateOf(false) }

                        /**
                         * Holds the current state of the AVD transition. While
                         * animating, it switches state with a delay of
                         * [AnimatedVectorIcon.durationMillis] after `targetAtEnd`.
                         *
                         * When this is `true`, we immediately switch to the static
                         * `finalIconContent` associated with the `finalState`. That
                         * way, the AVD leaves composition, and we prevent from
                         * animating in both directions.
                         */
                        var currentAtEnd by rememberSaveable { mutableStateOf(targetAtEnd) }

                        if (currentAtEnd) {
                            finalIconContent()
                        } else {
                            Icon(
                                icon = animatedIcon,
                                atEnd = targetAtEnd,
                                contentDescription = latestContentDescription(initialState),
                                modifier = modifier,
                                tint = latestTint,
                                size = latestSize,
                            )
                        }

                        // Update targetAtEnd whenever targetState changes and
                        // update currentState after the icon animation has completed.
                        LaunchedEffect(Unit) {
                            snapshotFlow { latestTargetState() }
                                .collectLatest { targetState ->
                                    targetAtEnd = targetState == finalState
                                    if (targetAtEnd) {
                                        delay(animatedIcon.durationMillis.toLong())
                                        currentState = targetState
                                    }
                                    currentAtEnd = targetAtEnd
                                }
                        }
                    }
                }
            }
        }
    }

    /**
     * [AnimatedContent] that becomes visible whenever there is no AVD
     * animating. Also [remember]ed as a tracking [movableContentOf] to avoid
     * recomposing the entire [AnimatedContent] whenever it becomes invisible.
     */
    val defaultContent = remember(icons) {
        movableContentOf {
            val transition = updateTransition(latestTargetState(), "AnimatedIcon")
            transition.AnimatedContent(
                transitionSpec = transitionSpec,
                contentKey = { it },
                contentAlignment = Alignment.Center,
                content = { targetState ->
                    getOrPutIconContent(targetState).invoke()
                }
            )

            // Update currentState whenever a transition has completed.
            LaunchedEffect(transition) {
                snapshotFlow { transition.currentState }
                    .collect { currentState = it }
            }
        }
    }

    /**
     * The latest composable content, [remember]ed as a [mutableStateOf] that
     * is changed whenever [targetState] returns a new value.
     */
    var content by remember { mutableStateOf(defaultContent) }
    LaunchedEffect(Unit) {
        snapshotFlow { latestTargetState() }.collect { targetState ->
            content = getOrPutIconTransition(currentState, targetState) ?: defaultContent
        }
    }
    content()
}

/**
 * Returns the value for the given [key] if the value is present and not `null`.
 * Otherwise, calls the [defaultValue] function, puts its result into the map if
 * it is not `null`, and returns the result.
 */
private inline fun <K, V> MutableMap<K, V>.getOrPutIfNotNull(
    key: K,
    defaultValue: () -> V?,
): V? = get(key) ?: defaultValue()?.also { put(key, it) }
