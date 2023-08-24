package com.example.porcent.widget

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent

object TipWidget : GlanceAppWidget() {
    val countKey = stringPreferencesKey(COUNT_KEY)
    val percentKey = stringPreferencesKey(PERCENT_KEY)
    val totalKey = stringPreferencesKey(TOTAL_KEY)
    val selection = stringPreferencesKey(SELECTION_KEY)

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme { PercentScreen() }
        }
    }
}

class SimpleCounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TipWidget
}
