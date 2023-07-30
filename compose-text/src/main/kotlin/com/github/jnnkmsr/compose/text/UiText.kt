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

package com.github.jnnkmsr.compose.text

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize

/**
 * Converts `this` [UiText] into a [String] that can be shown in the UI.
 *
 * @param args Format arguments for string resources.
 */
@Composable
@ReadOnlyComposable
@Keep
public fun UiText.get(vararg args: Any): String = when (this) {
    is StringText -> value
    is ResourceText -> stringResource(value, *args)
}

/**
 * Wrapper for text to be shown in the UI, allowing to pass text either as a
 * string or as a string resource ID.
 */
@Immutable
public sealed interface UiText : Parcelable {

    public companion object {

        /**
         * Creates and returns a [UiText] instance for the given [text].
         *
         * @param text The string content.
         */
        public operator fun invoke(text: String): UiText =
            StringText(text)

        /**
         * Creates and returns a [UiText] instance for the given string resource
         * ID.
         *
         * @param resId The string resource ID.
         */
        public operator fun invoke(@StringRes resId: Int): UiText =
            ResourceText(resId)
    }
}

/**
 * [UiText] wrapper for string text.
 *
 * Use [invoke][UiText.Companion.invoke] to create instances.
 */
@Parcelize
@JvmInline
public value class StringText internal constructor(
    internal val value: String,
) : UiText

/**
 * [UiText] wrapper for string resources.
 *
 * Use [invoke][UiText.Companion.invoke] to create instances.
 */
@Parcelize
@JvmInline
public value class ResourceText internal constructor(
    @StringRes internal val value: Int,
) : UiText
