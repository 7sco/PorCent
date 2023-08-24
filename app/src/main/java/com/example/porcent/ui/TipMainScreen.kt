package com.example.porcent.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.porcent.calculateTip
import com.example.porcent.ui.theme.PorCentTheme
import com.example.porcent.ui.theme.Strings
import com.example.porcent.ui.theme.ThemeColor
import com.example.porcent.ui.theme.ThemeTypo
import com.example.porcent.widget.EMPTY
import com.example.porcent.widget.Selection
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipMainScreen() {
    var splitValue by remember { mutableStateOf(1) }
    var splitValueBef by remember { mutableStateOf(0) }
    var billHolder by remember { mutableStateOf(EMPTY) }
    var tip by remember { mutableStateOf(EMPTY) }
    var selection by remember { mutableStateOf(Selection.BILL) }
    val format by remember {
        mutableStateOf(DecimalFormat("#.##").apply { roundingMode = RoundingMode.UP })
    }
    val total = remember {
        derivedStateOf {
            if (billHolder.isEmpty() || tip.isEmpty()) {
                format.format(BigDecimal.ZERO.toDouble())
            } else {
                format.format(calculateTip(billHolder, tip, splitValue))
            }
        }
    }

    val billBgColor by animateColorAsState(
        targetValue = if (selection == Selection.BILL)
            ThemeColor.inverseSurface.copy(.2f) else Color.Transparent,
        label = EMPTY
    )
    val tipBgColor by animateColorAsState(
        targetValue = if (selection == Selection.TIP)
            ThemeColor.inverseSurface.copy(.2f) else Color.Transparent,
        label = EMPTY
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeColor.background)
            .padding(16.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = Strings.tip_calculator),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = ThemeTypo.headlineMedium
                )
            },
            modifier = Modifier.wrapContentHeight(unbounded = true)
        )

        Box(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.align(Alignment.Center)) {

                BillTotalSection(
                    billHolder,
                    billBgColor,
                    clearBillHolder = { billHolder = EMPTY },
                    onSelection = { selection = Selection.BILL }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TipPercentSection(
                    tip = tip,
                    tipBgColor = tipBgColor,
                    clearTip = { tip = EMPTY },
                    onSelection = { selection = Selection.TIP }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SplitSection(
                    splitValue = splitValue,
                    splitValueBef = splitValueBef,
                    total.value
                ) { number ->
                    splitValueBef = splitValue
                    splitValue += number

                }
            }
        }

        NumbersSection { value ->
            if (selection == Selection.BILL) {
                val update = when (value) {
                    "<" -> if (billHolder.isNotEmpty()) billHolder.dropLast(1) else billHolder
                    else -> billHolder + value
                }

                billHolder = update
            } else {
                val update = when (value) {
                    "<" -> if (tip.isNotEmpty()) tip.dropLast(1) else tip
                    else -> tip + value
                }
                tip = update
            }
        }
    }
}

@Composable
fun BillTotalSection(
    billHolder: String,
    billBgColor: Color,
    clearBillHolder: () -> Unit,
    onSelection: () -> Unit
) {
    Row(modifier = Modifier
        .height(IntrinsicSize.Min)
        .clip(RoundedCornerShape(8.dp))
        .clickable { onSelection() }
        .background(billBgColor)
    ) {
        InformationSection(
            info = if (billHolder.isEmpty()) stringResource(id = Strings.default_total)
            else stringResource(id = Strings.total, billHolder),
            title = stringResource(id = Strings.bill_total)
        )

        DeleteButton(visible = billHolder.isNotEmpty(), onClick = clearBillHolder)
    }
}

@Composable
fun TipPercentSection(
    tip: String,
    tipBgColor: Color,
    clearTip: () -> Unit,
    onSelection: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelection() }
            .background(tipBgColor)
    ) {
        InformationSection(
            info = stringResource(id = Strings.default_tip, tip.ifEmpty { "0" }),
            title = stringResource(id = Strings.tip_percent)
        )

        DeleteButton(visible = tip.isNotEmpty(), onClick = clearTip)
    }
}

@Composable
fun RowScope.InformationSection(info: String, title: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp, top = 8.dp)
    ) {
        Text(text = title, style = ThemeTypo.labelLarge, color = ThemeColor.onSurfaceVariant)

        Text(
            text = info,
            style = ThemeTypo.displaySmall,
            color = ThemeColor.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
fun DeleteButton(visible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(visible = visible) {
        IconButton(onClick = onClick, modifier = Modifier.fillMaxHeight()) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = ThemeColor.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplitSection(
    splitValue: Int,
    splitValueBef: Int,
    total: String,
    updateSplitValue: (Int) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ThemeColor.surfaceVariant)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = Strings.split),
                modifier = Modifier,
                style = ThemeTypo.titleMedium,
                color = ThemeColor.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.align(Alignment.Start)) {
                FilledTonalIconButton(
                    onClick = { if (splitValue > 1) updateSplitValue(-1) },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    enabled = splitValue != 1,
                    content = {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                    }
                )

                AnimatedContent(
                    targetState = splitValue.toString(),
                    transitionSpec = {
                        if (splitValue > splitValueBef)
                            slideInVertically { -it } with slideOutVertically { it }
                        else slideInVertically { it } with slideOutVertically { -it }
                    }
                ) {
                    Text(
                        text = splitValue.toString(),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 8.dp),
                        style = ThemeTypo.displayMedium,
                        color = ThemeColor.primary,
                        softWrap = false
                    )
                }

                FilledTonalIconButton(
                    onClick = { updateSplitValue(1) },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    content = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
                )
            }

        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = Strings.split_total),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = ThemeTypo.titleMedium,
                color = ThemeColor.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = Strings.total, total),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = ThemeTypo.displayMedium,
                    color = ThemeColor.primary,
                    maxLines = 1
                )
                Text(
                    text = stringResource(id = Strings.total_each),
                    style = ThemeTypo.bodyMedium,
                    color = ThemeColor.primary,
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ColumnScope.NumbersSection(onButtonClick: (String) -> Unit) {
    Box(modifier = Modifier.weight(1f)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                (1..3).forEach {
                    NumberButton(it.toString()) { onButtonClick(it.toString()) }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                (4..6).forEach { NumberButton(it.toString()) { onButtonClick(it.toString()) } }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                (7..9).forEach { NumberButton(it.toString()) { onButtonClick(it.toString()) } }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                NumberButton("0") { onButtonClick("0") }

                FilledTonalButton(onClick = { onButtonClick(".") }) {
                    Box(modifier = Modifier) {
                        Text(
                            text = "1",
                            textAlign = TextAlign.Center,
                            style = ThemeTypo.displaySmall,
                            color = Color.Transparent,
                        )
                        Text(
                            text = ".",
                            textAlign = TextAlign.Center,
                            style = ThemeTypo.displaySmall,
                            color = ThemeColor.onPrimaryContainer,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
                FilledTonalButton(
                    onClick = { onButtonClick("<") },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = ThemeColor.tertiaryContainer,
                        contentColor = ThemeColor.onTertiaryContainer
                    )
                ) {
                    Box(modifier = Modifier) {
                        Text(
                            text = "1",
                            textAlign = TextAlign.Center,
                            style = ThemeTypo.displaySmall,
                            color = Color.Transparent,
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowBackIos,
                            contentDescription = null,
                            tint = ThemeColor.onPrimaryContainer,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(start = 8.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberButton(number: String, onButtonClick: () -> Unit) {
    FilledTonalButton(onClick = onButtonClick) {
        Text(
            text = number,
            textAlign = TextAlign.Center,
            style = ThemeTypo.displaySmall,
            color = ThemeColor.onPrimaryContainer
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun TipMainScreenPrev() {
    PorCentTheme {
        TipMainScreen()
    }
}



