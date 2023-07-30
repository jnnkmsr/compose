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

package com.github.jnnkmsr.compose.theme.token

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import com.github.jnnkmsr.compose.theme.Theme

/**
 * Material 3 [design token](http://m3.material.io/foundations/design-tokens/overview)
 * for [Typography] values.
 *
 * Tokens are used to reference theme values. To convert a token into a themed
 * [TextStyle], use [toTextStyle].
 */

@Immutable
public enum class TypographyToken {
    DisplayLarge,
    DisplayMedium,
    DisplaySmall,
    HeadlineLarge,
    HeadlineMedium,
    HeadlineSmall,
    TitleLarge,
    TitleMedium,
    TitleSmall,
    LabelLarge,
    LabelMedium,
    LabelSmall,
    BodyLarge,
    BodyMedium,
    BodySmall,
}

/**
 * Converts a [TypographyToken] to the local theme [TextStyle].
 */
@ReadOnlyComposable
@Composable
public fun TypographyToken.toTextStyle(): TextStyle = Theme.typography.toTextStyle(this)

/**
 * Converts a [TypographyToken] into a theme [TextStyle] using `this` themed
 * [Typography].
 */
public fun Typography.toTextStyle(token: TypographyToken): TextStyle = when (token) {
    TypographyToken.DisplayLarge -> displayLarge
    TypographyToken.DisplayMedium -> displayMedium
    TypographyToken.DisplaySmall -> displaySmall
    TypographyToken.HeadlineLarge -> headlineLarge
    TypographyToken.HeadlineMedium -> headlineMedium
    TypographyToken.HeadlineSmall -> headlineSmall
    TypographyToken.TitleLarge -> titleLarge
    TypographyToken.TitleMedium -> titleMedium
    TypographyToken.TitleSmall -> titleSmall
    TypographyToken.BodyLarge -> bodyLarge
    TypographyToken.BodyMedium -> bodyMedium
    TypographyToken.BodySmall -> bodySmall
    TypographyToken.LabelLarge -> labelLarge
    TypographyToken.LabelMedium -> labelMedium
    TypographyToken.LabelSmall -> labelSmall
}
