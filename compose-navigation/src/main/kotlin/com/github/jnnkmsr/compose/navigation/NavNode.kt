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

import androidx.compose.runtime.Stable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

/** Supertype for screens and dialogs that can be navigated to. */
@Stable
public sealed interface NavNode {

    /**
     * The node's unique route.
     */
    public val route: String

    /**
     * The list of arguments supported by the node.
     */
    public val arguments: List<NamedNavArgument>

    /**
     * The list of deep links associated with the node.
     */
    public val deepLinks: List<NavDeepLink>

    /**
     * Returns the no-argument route string for the node.
     */
    public fun destination(): String
}

/**
 * Base implementation for all [NavNode] types.
 */
public sealed class BaseNavNode protected constructor(
    /**
     * The unique path of the node's route string.
     */
    private val path: String,
    override val arguments: List<NamedNavArgument>,
    override val deepLinks: List<NavDeepLink>,
) : NavNode {

    final override val route: String by lazy {
        if (arguments.isEmpty()) {
            path
        } else {
            StringBuilder(path).run {
                val args = arguments.groupBy { (_, arg) ->
                    arg.isNullable || arg.isDefaultValuePresent
                }
                args[false]?.forEach { (name, _) -> append("/{$name}") }
                args[true]?.forEach { (name, _) -> append("?$name={$name}") }
                toString()
            }
        }
    }

    final override fun destination(): String = path

    final override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is BaseNavNode -> false
        else -> path == other.path
    }

    final override fun hashCode(): Int = path.hashCode()

    override fun toString(): String = "NavNode($path)"
}
