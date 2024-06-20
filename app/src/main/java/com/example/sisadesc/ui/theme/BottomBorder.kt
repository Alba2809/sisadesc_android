package com.example.sisadesc.ui.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.bottomBorder(strokeWidth: Dp, color: Color): Modifier = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

enum class Directions{
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
}
@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.customBorder(strokeWidth: Dp, color: Color, directions: List<Directions>): Modifier = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val end = size.width
            val heightBottom = size.height - strokeWidthPx / 2
            val heightTop = strokeWidthPx / 2

            if(directions.contains(Directions.TOP)){
                drawLine(
                    color = color,
                    start = Offset(x = 0f, y = heightTop),
                    end = Offset(x = end, y = heightTop),
                    strokeWidth = strokeWidthPx
                )
            }
            if(directions.contains(Directions.BOTTOM)){
                drawLine(
                    color = color,
                    start = Offset(x = 0f, y = heightBottom),
                    end = Offset(x = end, y = heightBottom),
                    strokeWidth = strokeWidthPx
                )
            }
            if(directions.contains(Directions.LEFT)){
                drawLine(
                    color = color,
                    start = Offset(x = 0f, y = heightTop),
                    end = Offset(x = 0f, y = heightBottom),
                    strokeWidth = strokeWidthPx
                )
            }
            if(directions.contains(Directions.RIGHT)){
                drawLine(
                    color = color,
                    start = Offset(x = end, y = heightTop),
                    end = Offset(x = end, y = heightBottom),
                    strokeWidth = strokeWidthPx
                )
            }
        }
    }
)