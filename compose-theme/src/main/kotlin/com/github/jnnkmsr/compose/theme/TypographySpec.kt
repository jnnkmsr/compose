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

package com.github.jnnkmsr.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.takeOrElse

/**
 * Returns a [Typography][androidx.compose.material3.Typography] from
 * [TypographySpec] input.
 *
 * @param default Default [TypographySpec] used as a fallback for all styles.
 * @param headings Heading style used for `display`, `headline` and `title`
 *   style. If not provided, [default] will be used instead.
 * @param display Style used for `displayLarge`, `displayMedium` and
 *   `displaySmall`. If not provided, [headings] will be used instead.
 * @param headline Style used for `headlineLarge`, `headlineMedium` and
 *   `headlineSmall`. If not provided, [headings] will be used instead.
 * @param title Style used for `titleLarge`, `titleMedium` and `titleSmall`.
 *   If not provided, [headings] will be used instead.
 * @param label Style used for `labelLarge`, `labelMedium` and `labelSmall`.
 *   If not provided, [default] will be used instead.
 * @param body Style used for `bodyLarge`, `bodyMedium` and `bodySmall`.
 *   If not provided, [default] will be used instead.
 * @param displayLarge Style used for `displayLarge`. If not provided, [display]
 *   will be used instead.
 * @param displayMedium Style used for `displayMedium`. If not provided,
 *   [display] will be used instead.
 * @param displaySmall Style used for `displaySmall`. If not provided, [display]
 *   will be used instead.
 * @param headlineLarge Style used for `headlineLarge`. If not provided,
 *   [headline] will be used instead.
 * @param headlineMedium Style used for `headlineMedium`. If not provided,
 *   [headline] will be used instead.
 * @param headlineSmall Style used for `headlineSmall`. If not provided,
 *   [headline] will be used instead.
 * @param titleLarge Style used for `titleLarge`. If not provided, [title]
 *   will be used instead.
 * @param titleMedium Style used for `titleMedium`. If not provided, [title]
 *   will be used instead.
 * @param titleSmall Style used for `titleSmall`. If not provided, [title]
 *   will be used instead.
 * @param labelLarge Style used for `labelLarge`. If not provided, [label]
 *   will be used instead.
 * @param labelMedium Style used for `labelMedium`. If not provided, [label]
 *   will be used instead.
 * @param labelSmall Style used for `labelSmall`. If not provided, [label]
 *   will be used instead.
 * @param bodyLarge Style used for `bodyLarge`. If not provided, [body]
 *   will be used instead.
 * @param bodyMedium Style used for `bodyMedium`. If not provided, [body]
 *   will be used instead.
 * @param bodySmall Style used for `bodySmall`. If not provided, [body]
 *   will be used instead.
 */
public fun Typography(
    default: TypographySpec,
    headings: TypographySpec = default,
    display: TypographySpec = headings,
    headline: TypographySpec = headings,
    title: TypographySpec = headings,
    label: TypographySpec = default,
    body: TypographySpec = default,
    displayLarge: TypographySpec = display,
    displayMedium: TypographySpec = display,
    displaySmall: TypographySpec = display,
    headlineLarge: TypographySpec = headline,
    headlineMedium: TypographySpec = headline,
    headlineSmall: TypographySpec = headline,
    titleLarge: TypographySpec = title,
    titleMedium: TypographySpec = title,
    titleSmall: TypographySpec = title,
    labelLarge: TypographySpec = label,
    labelMedium: TypographySpec = label,
    labelSmall: TypographySpec = label,
    bodyLarge: TypographySpec = body,
    bodyMedium: TypographySpec = body,
    bodySmall: TypographySpec = body,
): Typography = Typography(
    displayLarge = displayLarge.displayLarge.takeOrElse(
        lineHeight = TypographyDefaults.DisplayLargeLineHeight,
        fontScale = displayLarge.fontScale,
        fontSize = TypographyDefaults.DisplayLargeFontSize,
        letterSpacing = TypographyDefaults.DisplayLargeTracking,
        fontWeight = TypographyDefaults.DisplayLargeFontWeight,
    ),
    displayMedium = displayMedium.displayMedium.takeOrElse(
        lineHeight = TypographyDefaults.DisplayMediumLineHeight,
        fontScale = displayMedium.fontScale,
        fontSize = TypographyDefaults.DisplayMediumFontSize,
        letterSpacing = TypographyDefaults.DisplayMediumTracking,
        fontWeight = TypographyDefaults.DisplayMediumFontWeight,
    ),
    displaySmall = displaySmall.displaySmall.takeOrElse(
        lineHeight = TypographyDefaults.DisplaySmallLineHeight,
        fontScale = displaySmall.fontScale,
        fontSize = TypographyDefaults.DisplaySmallFontSize,
        letterSpacing = TypographyDefaults.DisplaySmallTracking,
        fontWeight = TypographyDefaults.DisplaySmallFontWeight,
    ),
    headlineLarge = headlineLarge.headlineLarge.takeOrElse(
        lineHeight = TypographyDefaults.HeadlineLargeLineHeight,
        fontScale = headlineLarge.fontScale,
        fontSize = TypographyDefaults.HeadlineLargeFontSize,
        letterSpacing = TypographyDefaults.HeadlineLargeTracking,
        fontWeight = TypographyDefaults.HeadlineLargeFontWeight,
    ),
    headlineMedium = headlineMedium.headlineMedium.takeOrElse(
        lineHeight = TypographyDefaults.HeadlineMediumLineHeight,
        fontScale = headlineMedium.fontScale,
        fontSize = TypographyDefaults.HeadlineMediumFontSize,
        letterSpacing = TypographyDefaults.HeadlineMediumTracking,
        fontWeight = TypographyDefaults.HeadlineMediumFontWeight,
    ),
    headlineSmall = headlineSmall.headlineSmall.takeOrElse(
        lineHeight = TypographyDefaults.HeadlineSmallLineHeight,
        fontScale = headlineSmall.fontScale,
        fontSize = TypographyDefaults.HeadlineSmallFontSize,
        letterSpacing = TypographyDefaults.HeadlineSmallTracking,
        fontWeight = TypographyDefaults.HeadlineSmallFontWeight,
    ),
    titleLarge = titleLarge.titleLarge.takeOrElse(
        lineHeight = TypographyDefaults.TitleLargeLineHeight,
        fontScale = titleLarge.fontScale,
        fontSize = TypographyDefaults.TitleLargeFontSize,
        letterSpacing = TypographyDefaults.TitleLargeTracking,
        fontWeight = TypographyDefaults.TitleLargeFontWeight,
    ),
    titleMedium = titleMedium.titleMedium.takeOrElse(
        lineHeight = TypographyDefaults.TitleMediumLineHeight,
        fontScale = titleMedium.fontScale,
        fontSize = TypographyDefaults.TitleMediumFontSize,
        letterSpacing = TypographyDefaults.TitleMediumTracking,
        fontWeight = TypographyDefaults.TitleMediumFontWeight,
    ),
    titleSmall = titleSmall.titleSmall.takeOrElse(
        lineHeight = TypographyDefaults.TitleSmallLineHeight,
        fontScale = titleSmall.fontScale,
        fontSize = TypographyDefaults.TitleSmallFontSize,
        letterSpacing = TypographyDefaults.TitleSmallTracking,
        fontWeight = TypographyDefaults.TitleSmallFontWeight,
    ),
    labelLarge = labelLarge.labelLarge.takeOrElse(
        lineHeight = TypographyDefaults.LabelLargeLineHeight,
        fontScale = labelLarge.fontScale,
        fontSize = TypographyDefaults.LabelLargeFontSize,
        letterSpacing = TypographyDefaults.LabelLargeTracking,
        fontWeight = TypographyDefaults.LabelLargeFontWeight,
    ),
    labelMedium = labelMedium.labelMedium.takeOrElse(
        lineHeight = TypographyDefaults.LabelMediumLineHeight,
        fontScale = labelMedium.fontScale,
        fontSize = TypographyDefaults.LabelMediumFontSize,
        letterSpacing = TypographyDefaults.LabelMediumTracking,
        fontWeight = TypographyDefaults.LabelMediumFontWeight,
    ),
    labelSmall = labelSmall.labelSmall.takeOrElse(
        lineHeight = TypographyDefaults.LabelSmallLineHeight,
        fontScale = labelSmall.fontScale,
        fontSize = TypographyDefaults.LabelSmallFontSize,
        letterSpacing = TypographyDefaults.LabelSmallTracking,
        fontWeight = TypographyDefaults.LabelSmallFontWeight,
    ),
    bodyLarge = bodyLarge.bodyLarge.takeOrElse(
        lineHeight = TypographyDefaults.BodyLargeLineHeight,
        fontScale = bodyLarge.fontScale,
        fontSize = TypographyDefaults.BodyLargeFontSize,
        letterSpacing = TypographyDefaults.BodyLargeTracking,
        fontWeight = TypographyDefaults.BodyLargeFontWeight,
    ),
    bodyMedium = bodyMedium.bodyMedium.takeOrElse(
        lineHeight = TypographyDefaults.BodyMediumLineHeight,
        fontScale = bodyMedium.fontScale,
        fontSize = TypographyDefaults.BodyMediumFontSize,
        letterSpacing = TypographyDefaults.BodyMediumTracking,
        fontWeight = TypographyDefaults.BodyMediumFontWeight,
    ),
    bodySmall = bodySmall.bodySmall.takeOrElse(
        lineHeight = TypographyDefaults.BodySmallLineHeight,
        fontScale = bodySmall.fontScale,
        fontSize = TypographyDefaults.BodySmallFontSize,
        letterSpacing = TypographyDefaults.BodySmallTracking,
        fontWeight = TypographyDefaults.BodySmallFontWeight,
    ),
)

/**
 * Convenience class to be used for specifying one or multiple styles of a
 * theme's [Typography].
 */
@Immutable
public open class TypographySpec(
    /** The [FontFamily] of the specified style(s). */
    private val fontFamily: FontFamily,
    /** An optional scale factor to compensate for a font's optical scaling. */
    internal val fontScale: Float = 1f,
    /**
     * An optional [FontSynthesis] to apply to by default when calling
     * [textStyle].
     */
    private val fontSynthesis: FontSynthesis? = null,
    /**
     * An optional [BaselineShift] to apply to by default when calling
     * [textStyle].
     */
    private val baselineShift: BaselineShift? = null,
) {
    /** Material 3 'Display large' style. */
    public open val displayLarge: TextStyle get() = textStyle()

    /** Material 3 'Display medium' style. */
    public open val displayMedium: TextStyle get() = textStyle()

    /** Material 3 'Display small' style. */
    public open val displaySmall: TextStyle get() = textStyle()

    /** Material 3 'Headline large' style. */
    public open val headlineLarge: TextStyle get() = textStyle()

    /** Material 3 'Headline medium' style. */
    public open val headlineMedium: TextStyle get() = textStyle()

    /** Material 3 'Headline small' style. */
    public open val headlineSmall: TextStyle get() = textStyle()

    /** Material 3 'Title large' style. */
    public open val titleLarge: TextStyle get() = textStyle()

    /** Material 3 'Title medium' style. */
    public open val titleMedium: TextStyle get() = textStyle()

    /** Material 3 'Title small' style. */
    public open val titleSmall: TextStyle get() = textStyle()

    /** Material 3 'Label large' style. */
    public open val labelLarge: TextStyle get() = textStyle()

    /** Material 3 'Label medium' style. */
    public open val labelMedium: TextStyle get() = textStyle()

    /** Material 3 'Label small' style. */
    public open val labelSmall: TextStyle get() = textStyle()

    /** Material 3 'Body large' style. */
    public open val bodyLarge: TextStyle get() = textStyle()

    /** Material 3 'Body medium' style. */
    public open val bodyMedium: TextStyle get() = textStyle()

    /** Material 3 'Body small' style. */
    public open val bodySmall: TextStyle get() = textStyle()

    /**
     * Returns a customizable [TextStyle] with all properties being unspecified
     * by default. Implementing classes may use this function to override
     * Material text styles.
     */
    protected fun textStyle(
        fontFamily: FontFamily = this.fontFamily,
        fontSize: TextUnit = TextUnit.Unspecified,
        lineHeight: TextUnit = TextUnit.Unspecified,
        fontWeight: FontWeight? = null,
        fontStyle: FontStyle? = null,
        fontSynthesis: FontSynthesis? = this.fontSynthesis,
        fontFeatureSettings: String? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        baselineShift: BaselineShift? = this.baselineShift,
    ): TextStyle = TextStyle(
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontSynthesis = fontSynthesis,
        fontFamily = fontFamily,
        fontFeatureSettings = fontFeatureSettings,
        letterSpacing = letterSpacing,
        baselineShift = baselineShift,
        lineHeight = lineHeight,
    )

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is TypographySpec -> false
        fontFamily != other.fontFamily -> false
        fontScale != other.fontScale -> false
        fontSynthesis != other.fontSynthesis -> false
        else -> baselineShift == other.baselineShift
    }

    override fun hashCode(): Int {
        var result = fontFamily.hashCode()
        result = 31 * result + fontScale.hashCode()
        result = 31 * result + (fontSynthesis?.hashCode() ?: 0)
        result = 31 * result + (baselineShift?.hashCode() ?: 0)
        return result
    }
}

/**
 * Returns a copy of `this` [TextStyle] using the given parameters to replace
 * [Unspecified][TextUnit.Unspecified] or `null` values. The additional
 * [fontScale] input is always used to increase or decrease to output style's
 * [fontSize][TextStyle.fontSize].
 */
private fun TextStyle.takeOrElse(
    fontScale: Float = 1f,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    letterSpacing: TextUnit = 0.sp,
    lineHeight: TextUnit = TextUnit.Unspecified,
) = copy(
    fontSize = this.fontSize.takeOrElse { fontSize } * fontScale,
    fontWeight = this.fontWeight ?: fontWeight,
    lineHeight = this.lineHeight.takeOrElse { lineHeight },
    letterSpacing = this.letterSpacing.takeOrElse { letterSpacing },
)

/**
 * Holder for default
 * [Material 3](https://m3.material.io/styles/typography/type-scale-tokens#40fc37f8-3269-4aa3-9f28-c6113079ac5d).
 * typography values.
 */
@Immutable
private object TypographyDefaults {
    val DisplayLargeLineHeight = 64.sp
    val DisplayLargeFontSize = 57.sp
    val DisplayLargeTracking = (-0.25f).sp
    val DisplayLargeFontWeight get() = FontWeight.Normal
    val DisplayMediumLineHeight = 52.sp
    val DisplayMediumFontSize = 45.sp
    val DisplayMediumTracking = 0.sp
    val DisplayMediumFontWeight get() = FontWeight.Normal
    val DisplaySmallLineHeight = 44.sp
    val DisplaySmallFontSize = 36.sp
    val DisplaySmallTracking = 0.sp
    val DisplaySmallFontWeight get() = FontWeight.Normal
    val HeadlineLargeLineHeight = 40.sp
    val HeadlineLargeFontSize = 32.sp
    val HeadlineLargeTracking = 0.sp
    val HeadlineLargeFontWeight get() = FontWeight.Normal
    val HeadlineMediumLineHeight = 36.sp
    val HeadlineMediumFontSize = 28.sp
    val HeadlineMediumTracking = 0.sp
    val HeadlineMediumFontWeight get() = FontWeight.Normal
    val HeadlineSmallLineHeight = 32.sp
    val HeadlineSmallFontSize = 24.sp
    val HeadlineSmallTracking = 0.sp
    val HeadlineSmallFontWeight get() = FontWeight.Normal
    val TitleLargeLineHeight = 28.sp
    val TitleLargeFontSize = 22.sp
    val TitleLargeTracking = 0.sp
    val TitleLargeFontWeight get() = FontWeight.Normal
    val TitleMediumLineHeight = 24.sp
    val TitleMediumFontSize = 16.sp
    val TitleMediumTracking = (0.15f).sp
    val TitleMediumFontWeight get() = FontWeight.Medium
    val TitleSmallLineHeight = 20.sp
    val TitleSmallFontSize = 14.sp
    val TitleSmallTracking = (0.1f).sp
    val TitleSmallFontWeight get() = FontWeight.Medium
    val LabelLargeLineHeight = 20.sp
    val LabelLargeFontSize = 14.sp
    val LabelLargeTracking = (0.1f).sp
    val LabelLargeFontWeight get() = FontWeight.Medium
    val LabelMediumLineHeight = 16.sp
    val LabelMediumFontSize = 12.sp
    val LabelMediumTracking = (0.5f).sp
    val LabelMediumFontWeight get() = FontWeight.Medium
    val LabelSmallLineHeight = 16.sp
    val LabelSmallFontSize = 11.sp
    val LabelSmallTracking = (0.5f).sp
    val LabelSmallFontWeight get() = FontWeight.Medium
    val BodyLargeLineHeight = 24.sp
    val BodyLargeFontSize = 16.sp
    val BodyLargeTracking = (0.5f).sp
    val BodyLargeFontWeight get() = FontWeight.Normal
    val BodyMediumLineHeight = 20.sp
    val BodyMediumFontSize = 14.sp
    val BodyMediumTracking = (0.25f).sp
    val BodyMediumFontWeight get() = FontWeight.Normal
    val BodySmallLineHeight = 16.sp
    val BodySmallFontSize = 12.sp
    val BodySmallTracking = (0.4f).sp
    val BodySmallFontWeight get() = FontWeight.Normal
}
