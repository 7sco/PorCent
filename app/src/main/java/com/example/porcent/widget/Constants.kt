package com.example.porcent.widget

import androidx.compose.runtime.Composable
import androidx.glance.ButtonColors
import androidx.glance.ButtonDefaults
import com.example.porcent.ui.theme.GlanceColor

const val COUNT_KEY = "count"
const val PERCENT_KEY = "percent"
const val TOTAL_KEY = "total"
const val SELECTION_KEY = "selection"

const val EMPTY = ""
const val ZERO = "0"

val numberColor: ButtonColors
    @Composable get() {
        return ButtonDefaults.buttonColors(
            backgroundColor = GlanceColor.primaryContainer,
            contentColor = GlanceColor.onPrimaryContainer
        )
    }

enum class Selection(val value: String){
    BILL("bill"),
    TIP("tip")
}