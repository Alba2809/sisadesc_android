package com.example.sisadesc.ui.events

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sisadesc.core.model.Event
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel? = EventsViewModel()
) {
    val getDataState by eventsViewModel!!.getDataState.observeAsState(initial = GetDataState())
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendarState = rememberSelectableCalendarState()

    LaunchedEffect(Unit) {
        val currentDate = LocalDate.now()
        eventsViewModel!!.getEvents(year = currentDate.year, month = currentDate.monthValue)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
        ) {
            if (!getDataState.isLoading) {
                SelectableCalendar(
                    modifier = Modifier.background(Color(0xFFFFFFFB)),
                    calendarState = calendarState,
                    dayContent = { dayState ->
                        MyDay(dayState = dayState) {
                            try {
                                val events: List<Event> = getDataState.events.filter { event ->
                                    val formattedDate = formatter.format(event.startTime.toDate())
                                    formattedDate.equals(dayState.date.toString())
                                }
                                events
                            } catch (e: Exception) {
                                println("Error parsing date: ${e.message}")
                                emptyList()
                            }
                        }
                    }
                )
                SelectionControls(selectionState = calendarState.selectionState)
            } else Text(text = "Cargando...")

        }

    }
}

@Preview
@Composable
fun MyCalendarPreview() {
    val calState = rememberSelectableCalendarState()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp)
    ) {
        SelectableCalendar(
            modifier = Modifier.background(Color(0xFFFFFFFB)),
            calendarState = calState,
            dayContent = { dayState ->
                MyDay(dayState = dayState) {
                    val event = Event()
                    if (dayState.isCurrentDay) listOf(event)
                    else emptyList<Event>()
                }
            }
        )

        SelectionControls(selectionState = calState.selectionState)
    }
}

@Composable
fun MyDay(dayState: DayState<DynamicSelectionState>, getEvents: () -> List<Event>) {
    val events = getEvents()
    val backgroundColor =
        if (dayState.selectionState.isDateSelected(dayState.date)) Color(0xFFFFD3CF) else Color.Transparent
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    // Create an Animatable object to animate the offset
    val offsetY = remember { Animatable(0f) }

    // Launch a coroutine to perform the infinite animation
    LaunchedEffect(Unit) {
        launch {
            while (true) {
                offsetY.animateTo(
                    targetValue = 10f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
                delay(100) // Optional delay between animations
            }
        }
    }

    if (dayState.isFromCurrentMonth) {
        Button(
            onClick = {
                dayState.selectionState.selection = listOf(dayState.date)
            },
            modifier = Modifier
                .padding(1.dp)
                .size(50.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            ),
            border = if (dayState.isCurrentDay) BorderStroke(1.dp, Color.Gray) else null
        ) {
            Box {
                Text(
                    text = dayState.date.dayOfMonth.toString(),
                    color = if (events.isNotEmpty()) Color.Red else Color.Black,
                )
                if (events.isNotEmpty()) {
//                    InfiniteAnimationIcon(Modifier.align(Alignment.TopEnd))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Icono de estrella",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(15.dp)
                            .offset(y = (-10).dp, x = (8).dp),
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun InfiniteAnimationIcon(modifier: Modifier) {
    // Create an Animatable object to animate the offset
    val offsetY = remember { Animatable(0f) }

    // Launch a coroutine to perform the infinite animation
    LaunchedEffect(Unit) {
        launch {
            while (true) {
                offsetY.animateTo(
                    targetValue = -10f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
                offsetY.animateTo(
                    targetValue = -5f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
                delay(100) // Optional delay between animations
            }
        }
    }

    Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "Icono de estrella",
        modifier = modifier
            .size(15.dp)
            .offset(y = offsetY.value.dp, x = 8.dp),
        tint = Color.Red
    )
}

@Composable
private fun SelectionControls(
    selectionState: DynamicSelectionState,
) {
    Text(
        text = "Selection: ${selectionState.selection.joinToString { it.toString() }}",
    )
}