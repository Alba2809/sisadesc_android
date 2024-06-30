package com.example.sisadesc.ui.events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sisadesc.core.model.Event
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel? = EventsViewModel()
) {
    val events by eventsViewModel!!.events.observeAsState(initial = emptyList())
    val isLoading: Boolean by eventsViewModel!!.isLoading.observeAsState(initial = false)
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendarState = rememberSelectableCalendarState()

    val sheetState = rememberModalBottomSheetState(true)
    var isSheetShow by remember {
        mutableStateOf(false)
    }
    var eventsOfDay: List<Event>? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(calendarState.monthState.currentMonth) {
        println("LaunchedEffect: ${calendarState.monthState.currentMonth}")
        val yearSelected = calendarState.monthState.currentMonth.year
        val monthSelected = calendarState.monthState.currentMonth.monthValue

        eventsViewModel!!.getEvents(year = yearSelected, month = monthSelected)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        color = Color.White
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isLoading) {
                SelectableCalendar(
                    modifier = Modifier
//                        .background(Color(0xFFffffdf),RoundedCornerShape(10.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                        .padding(5.dp),
                    calendarState = calendarState,
                    dayContent = { dayState ->
                        val eventsDayState = events.filter { event ->
                            try {
                                val formattedDate =
                                    formatter.format(event.startTime.toDate())
                                formattedDate.equals(dayState.date.toString())
                            } catch (e: Exception) {
                                println("Error parsing date: ${e.message}")
                                false
                            }
                        }

                        MyDay(
                            dayState = dayState,
                            events = eventsDayState
                        ) { eventsOfDaySelected ->
                            eventsOfDay = eventsOfDaySelected
                            isSheetShow = true
                        }
                    }
                )
            } else LinearProgressIndicator()
        }
    }
    if (isSheetShow && eventsOfDay != null) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = Color.White,
            onDismissRequest = {
                isSheetShow = false
            },
            contentColor = Color.White
        ) {
            BottomSheetContent(eventsOfDay!!) {
                isSheetShow = false
            }
        }
    }
}

@Composable
fun BottomSheetContent(events: List<Event>, onHideSheetButton: () -> Unit) {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        HeaderEventData()

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(events) { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    FieldEventData(
                        modifier = Modifier.weight(1f),
                        value = event.description,
                    )
                    FieldEventData(
                        modifier = Modifier.width(100.dp),
                        value = formatter.format(event.startTime.toDate()),
                    )
                    FieldEventData(
                        modifier = Modifier.width(100.dp),
                        value = formatter.format(event.endTime.toDate()),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onHideSheetButton() },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Cerrar",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun HeaderEventData() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "Descripción",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Inicio",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Final",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FieldEventData(modifier: Modifier? = Modifier, value: String) {
    TextField(
        modifier = modifier!!,
        textStyle = TextStyle(fontSize = 15.sp),
        value = value,
        onValueChange = { },
        readOnly = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun MyDay(
    dayState: DayState<DynamicSelectionState>,
    events: List<Event>,
    onSelectDay: (List<Event>) -> Unit
) {
    println("MyDay(${dayState.date}): $events")
    val backgroundColor =
        if (dayState.selectionState.isDateSelected(dayState.date)) Color(0xFFFFD3CF) else Color.Transparent

    if (dayState.isFromCurrentMonth) {
        Button(
            onClick = {
                dayState.selectionState.selection = listOf(dayState.date)
                if (events.isNotEmpty()) onSelectDay(events)
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

