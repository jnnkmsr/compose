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
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize

/**
 * Wrapper for text to be shown in the UI, allowing to pass text either as a
 * string or as a string resource ID.
 */
@Immutable
public sealed interface UiText : Parcelable {

    /**
     * Converts `this` [UiText] into a `String` that can be shown in the UI.
     *
     * @param args Format arguments for string resources.
     */
    @Composable
    public operator fun invoke(vararg args: Any): String

    public companion object {

        /**
         * Creates and returns a [UiText] instance for the given [text].
         *
         * @param text The string content.
         */
        public operator fun invoke(text: String): UiText =
            StringUiText(text)

        /**
         * Creates and returns a [UiText] instance for the given string resource
         * ID.
         *
         * @param resId The string resource ID.
         */
        public operator fun invoke(@StringRes resId: Int): UiText =
            ResourceUiText(resId)
    }
}

/**
 * [UiText] wrapper for string text.
 *
 * Use [invoke][UiText.Companion.invoke] to create instances.
 */
@Parcelize
@JvmInline
public value class StringUiText internal constructor(
    private val value: String,
) : UiText {

    @Composable
    override fun invoke(vararg args: Any): String = value
}

/**
 * [UiText] wrapper for string resources.
 *
 * Use [invoke][UiText.Companion.invoke] to create instances.
 */
@Parcelize
@JvmInline
public value class ResourceUiText internal constructor(
    @StringRes private val value: Int,
) : UiText {

    @Composable
    override fun invoke(vararg args: Any): String = stringResource(value, *args)
}
