package com.example.porcent

import java.math.RoundingMode
import java.text.DecimalFormat

fun calculateTip(billTotal: String, tip: String, split: Int): Double {
    val billNum = billTotal.toBigDecimal().toDouble()
    val tipNum = tip.toBigDecimal().toDouble()
    val total = calculatePercentage(billNum, tipNum)
    return (billNum + total) / split
}

fun calculatePercentage(total: String, percent: String): String {
    val billNum = total.toBigDecimal().toDouble()
    val tipNum = percent.toBigDecimal().toDouble()
    val calculation = calculatePercentage(billNum, tipNum)
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.DOWN
    return df.format(calculation)
}

private fun calculatePercentage(total: Double, percent: Double): Double =
    (percent / 100.toDouble()) * total


