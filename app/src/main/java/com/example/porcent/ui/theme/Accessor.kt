package com.example.porcent.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.color.ColorProviders
import com.example.porcent.R

val ThemeColor : ColorScheme
    @Composable
    get() = MaterialTheme.colorScheme

val ThemeTypo : Typography
    @Composable
    get() = MaterialTheme.typography

val ThemeShapes : Shapes
    @Composable
    get() = MaterialTheme.shapes

val GlanceColor : ColorProviders
    @Composable
    get() = GlanceTheme.colors


typealias Strings = R.string