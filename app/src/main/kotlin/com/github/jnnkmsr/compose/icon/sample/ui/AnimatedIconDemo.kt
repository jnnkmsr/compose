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

package com.github.jnnkmsr.compose.icon.sample.ui

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.jnnkmsr.compose.icon.AnimatedIcon
import com.github.jnnkmsr.compose.icon.AnimatedVectorIcon
import com.github.jnnkmsr.compose.icon.StaticVectorIcon
import com.github.jnnkmsr.compose.icon.VectorIcon
import com.github.jnnkmsr.compose.icon.sample.R
import com.github.jnnkmsr.compose.icon.sample.ui.IconState.Close
import com.github.jnnkmsr.compose.icon.sample.ui.IconState.Done
import com.github.jnnkmsr.compose.icon.sample.ui.IconState.Filter
import com.github.jnnkmsr.compose.icon.sample.ui.IconState.Tune

private val FilterToDoneIcon = AnimatedVectorIcon(R.drawable.filter_to_done, 600)
private val DoneToFilterIcon = AnimatedVectorIcon(R.drawable.done_to_filter, 400)
private val TuneToCloseIcon = AnimatedVectorIcon(R.drawable.tune_to_close, 600)
private val CloseToTuneIcon = AnimatedVectorIcon(R.drawable.close_to_tune, 400)

@Immutable
enum class IconState(val icon: VectorIcon) {
    Filter(StaticVectorIcon(R.drawable.filter)),
    Done(StaticVectorIcon(R.drawable.done)),
    Tune(StaticVectorIcon(R.drawable.tune)),
    Close(StaticVectorIcon(R.drawable.close)),
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedIconDemo(
    modifier: Modifier = Modifier,
    initialState: IconState = Filter,
) {
    var state by rememberSaveable { mutableStateOf(initialState) }
    var tint by remember { mutableStateOf(Color.Red) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedIcon(
                targetState = { state },
                icons = { state.icon },
                iconTransitionSpec = { initialState, finalState ->
                    when {
                        initialState == Filter && finalState == Done -> FilterToDoneIcon
                        initialState == Done && finalState == Filter -> DoneToFilterIcon
                        initialState == Tune && finalState == Close -> TuneToCloseIcon
                        initialState == Close && finalState == Tune -> CloseToTuneIcon
                        else -> null
                    }
                },
                contentDescription = { null },
                tint = tint,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconState.values().forEach { targetState ->
                TextButton(
                    onClick = {
                        state = targetState
                        tint = if (tint == Color.Red) Color.Blue else Color.Red
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(targetState.name)
                }
            }
        }
    }
}
