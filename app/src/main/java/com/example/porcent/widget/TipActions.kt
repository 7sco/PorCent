package com.example.porcent.widget

import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.example.porcent.calculatePercentage

private fun interface CallbackAction : ActionCallback

/**
 * Number-Pad Actions
 */
object Zero : ActionCallback by callbackAction(value = 0)
object One : ActionCallback by callbackAction(value = 1)
object Two : ActionCallback by callbackAction(value = 2)
object Three : ActionCallback by callbackAction(value = 3)
object Four : ActionCallback by callbackAction(value = 4)
object Five : ActionCallback by callbackAction(value = 5)
object Six : ActionCallback by callbackAction(value = 6)
object Seven : ActionCallback by callbackAction(value = 7)
object Eight : ActionCallback by callbackAction(value = 8)
object Nine : ActionCallback by callbackAction(value = 9)

private fun callbackAction(value: Int) = CallbackAction { context, glanceId, _ ->
    updateAppWidgetState(context, glanceId) { prefs ->
        val currentSelection = prefs[TipWidget.selection] ?: Selection.BILL.value

        if (currentSelection == Selection.BILL.value) {
            var number = prefs[TipWidget.countKey] ?: EMPTY
            if (number == ZERO) number = EMPTY
            prefs[TipWidget.countKey] = number + value.toString()
        } else {
            var number = prefs[TipWidget.percentKey] ?: EMPTY
            if (number == ZERO) number = EMPTY
            prefs[TipWidget.percentKey] = number + value.toString()
        }
        calculateTotal(prefs)
    }
    TipWidget.update(context, glanceId)
}

/**
 * Box Selection Action
 */

object BillSelection : ActionCallback by selectionAction(Selection.BILL)
object TipSelection : ActionCallback by selectionAction(Selection.TIP)

private fun selectionAction(selection: Selection) = CallbackAction { context, glanceId, _ ->
    updateAppWidgetState(context, glanceId) { prefs ->
        prefs[TipWidget.selection] = selection.value
    }
    TipWidget.update(context, glanceId)
}

/**
 * Decimal Selection Action
 */

object DecimalAction : ActionCallback by (CallbackAction { context, glanceId, _ ->
    updateAppWidgetState(context, glanceId) { prefs ->
        val currentSelection = prefs[TipWidget.selection] ?: Selection.BILL.value
        if (currentSelection == Selection.BILL.value) {
            val number = prefs[TipWidget.countKey] ?: ZERO
            if (!number.contains(".")) {
                prefs[TipWidget.countKey] = "$number."
                calculateTotal(prefs)
            }
        } else {
            val number = prefs[TipWidget.percentKey] ?: ZERO
            if (!number.contains(".")) {
                prefs[TipWidget.percentKey] = "$number."
                calculateTotal(prefs)
            }
        }
    }
    TipWidget.update(context, glanceId)
})

/**
 * Clear inputs actions
 */

object ClearBillAction : ActionCallback by (CallbackAction { context, glanceId, _ ->
    updateAppWidgetState(context, glanceId) { prefs ->
        prefs[TipWidget.countKey] = ZERO
        calculateTotal(prefs)
    }
    TipWidget.update(context, glanceId)
})

object ClearPercentAction : ActionCallback by (CallbackAction { context, glanceId, _ ->
    updateAppWidgetState(context, glanceId) { prefs ->
        prefs[TipWidget.percentKey] = ZERO
        calculateTotal(prefs)
    }
    TipWidget.update(context, glanceId)
})

/**
 * Delete input numbers action
 */

object RemoveAction : ActionCallback by (CallbackAction { context, glanceId, _ ->
    updateAppWidgetState(context, glanceId) { prefs ->
        val currentSelection = prefs[TipWidget.selection] ?: Selection.BILL.value
        if (currentSelection == Selection.BILL.value) {
            var number = prefs[TipWidget.countKey] ?: ZERO
            number = if (number.isNotEmpty()) {
                val temp = number.dropLast(1)
                temp.ifEmpty { ZERO }
            } else ZERO
            prefs[TipWidget.countKey] = number
        } else {
            var number = prefs[TipWidget.percentKey] ?: ZERO
            number = if (number.isNotEmpty()) {
                val temp = number.dropLast(1)
                temp.ifEmpty { ZERO }
            } else ZERO
            prefs[TipWidget.percentKey] = number
        }

        calculateTotal(prefs)
    }
    TipWidget.update(context, glanceId)
})

private fun calculateTotal(prefs: MutablePreferences) {
    val bill = prefs[TipWidget.countKey] ?: ZERO
    val tip = prefs[TipWidget.percentKey] ?: ZERO

    val total = calculatePercentage(bill, tip)

    prefs[TipWidget.totalKey] = total
}