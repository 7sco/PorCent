package com.example.porcent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.porcent.ui.TipMainScreen
import com.example.porcent.ui.theme.PorCentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PorCentTheme {
                TipMainScreen()
            }
        }
    }
}
