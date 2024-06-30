package com.example.sisadesc.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisadesc.core.model.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.launch
import java.util.Calendar

data class GetDataState(val isLoading: Boolean = false, val events: List<Event> = emptyList())

class EventsViewModel : ViewModel() {
    private val _getDataState = MutableLiveData<GetDataState>().apply {
        value = GetDataState()
    }
    val getDataState: LiveData<GetDataState> = _getDataState

    fun getEvents(year: Int, month: Int) {
        viewModelScope.launch {
            try {
                _getDataState.value = _getDataState.value?.copy(isLoading = true)

                val (startOfMonth, endOfMonth) = getDateRange(year, month)

                FirebaseFirestore.getInstance()
                    .collection("events")
                    .whereGreaterThanOrEqualTo("startTime", startOfMonth)
                    .whereLessThanOrEqualTo("endTime", endOfMonth)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents != null) {
                            val eventsList = documents.toObjects<Event>()
                            println(eventsList)
                            _getDataState.value =
                                GetDataState(isLoading = false, events = eventsList)
                        }
                    }
                    .addOnFailureListener {
                        println(it.message)
                        _getDataState.value = GetDataState(isLoading = false, events = emptyList())
                    }
            } catch (e: Exception) {
                println(e.message)
                _getDataState.value = GetDataState(isLoading = false, events = emptyList())
            }

        }
    }

    private fun getDateRange(year: Int, month: Int): Pair<Timestamp, Timestamp> {
        val calendar = Calendar.getInstance()

        calendar.set(year, month - 1, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.time

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endOfMonth = calendar.time

        return Pair(Timestamp(startOfMonth), Timestamp(endOfMonth))
    }
}