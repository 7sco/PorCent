package com.example.porcent.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.porcent.ui.theme.GlanceColor

@Composable
fun PercentScreen() {
    val count = currentState(key = TipWidget.countKey) ?: 0
    val percent = currentState(key = TipWidget.percentKey) ?: ZERO
    val total = currentState(key = TipWidget.totalKey) ?: ZERO
    val selection = currentState(key = TipWidget.selection) ?: Selection.BILL.value

    androidx.glance.layout.Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(GlanceColor.surface)
            .cornerRadius(16.dp),
    ) {
        androidx.glance.layout.Column(
            modifier = GlanceModifier
                .background(GlanceColor.surfaceVariant)
                .cornerRadius(16.dp)
                .padding(8.dp)
        ) {
            InformationSection(
                text = "$ $count",
                selected = selection == Selection.BILL.value,
                showClear = count.toString().length > 1,
                action = actionRunCallback(BillSelection::class.java),
                clearAction = actionRunCallback(ClearBillAction::class.java)
            )

            InformationSection(
                text = "% $percent",
                selected = selection == Selection.TIP.value,
                showClear = percent.length > 1,
                action = actionRunCallback(TipSelection::class.java),
                clearAction = actionRunCallback(ClearPercentAction::class.java),
            )

            Row {
                androidx.glance.text.Text(
                    text = "=",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = GlanceColor.onSurfaceVariant,
                        fontSize = 26.sp,
                    ),
                    maxLines = 1
                )
                androidx.glance.text.Text(
                    modifier = GlanceModifier.defaultWeight().fillMaxWidth(),
                    text = total,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = GlanceColor.onSurfaceVariant,
                        fontSize = 26.sp,
                        textAlign = TextAlign.End
                    ),
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = GlanceModifier.height(4.dp))
        androidx.glance.layout.Column(modifier = GlanceModifier.padding(8.dp)) {

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "1",
                    onClick = actionRunCallback(One::class.java),
                    colors = numberColor
                )
                Spacer(modifier = GlanceModifier.width(8.dp))

                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "2",
                    onClick = actionRunCallback(Two::class.java),
                    colors = numberColor
                )

                Spacer(modifier = GlanceModifier.width(8.dp))

                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "3",
                    onClick = actionRunCallback(Three::class.java),
                    colors = numberColor
                )
            }

            Spacer(modifier = GlanceModifier.height(16.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "4",
                    onClick = actionRunCallback(Four::class.java),
                    colors = numberColor
                )
                Spacer(modifier = GlanceModifier.width(8.dp))

                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "5",
                    onClick = actionRunCallback(Five::class.java),
                    colors = numberColor
                )
                Spacer(modifier = GlanceModifier.width(8.dp))

                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "6",
                    onClick = actionRunCallback(Six::class.java),
                    colors = numberColor
                )
            }

            Spacer(modifier = GlanceModifier.height(16.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "7",
                    onClick = actionRunCallback(Seven::class.java),
                    colors = numberColor
                )
                Spacer(modifier = GlanceModifier.width(8.dp))

                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "8",
                    onClick = actionRunCallback(Eight::class.java),
                    colors = numberColor
                )
                Spacer(modifier = GlanceModifier.width(8.dp))

                Button(
                    modifier = GlanceModifier.cornerRadius(20.dp).defaultWeight(),
                    text = "9",
                    onClick = actionRunCallback(Nine::class.java),
                    colors = numberColor
                )
            }

            Spacer(modifier = GlanceModifier.height(16.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = ZERO,
                    onClick = actionRunCallback(Zero::class.java),
                    colors = numberColor
                )

                Spacer(modifier = GlanceModifier.width(8.dp))
                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = ".",
                    onClick = actionRunCallback(DecimalAction::class.java),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GlanceColor.tertiaryContainer,
                        contentColor = GlanceColor.onTertiaryContainer
                    )
                )

                Spacer(modifier = GlanceModifier.width(8.dp))

                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = "<",
                    onClick = actionRunCallback(RemoveAction::class.java),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GlanceColor.secondaryContainer,
                        contentColor = GlanceColor.onSecondaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun InformationSection(
    text: String,
    selected: Boolean,
    showClear: Boolean,
    action: Action,
    clearAction: Action
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth()
            .background(if (selected) GlanceColor.inverseOnSurface else GlanceColor.surfaceVariant)
            .padding(4.dp)
            .cornerRadius(16.dp)
            .clickable(action)
    ) {
        androidx.glance.text.Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                color = if (selected) GlanceColor.onSurface else GlanceColor.onSurfaceVariant,
                fontSize = 26.sp,
                textAlign = TextAlign.Start
            ),
            maxLines = 1,
            modifier = GlanceModifier.defaultWeight()
        )
        if (showClear) {
            androidx.glance.text.Text(
                "x",
                modifier = GlanceModifier
                    .padding(4.dp)
                    .cornerRadius(24.dp)
                    .clickable(clearAction),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = if (selected) GlanceColor.onSurface else GlanceColor.onSurfaceVariant,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
            )
        }

    }
}