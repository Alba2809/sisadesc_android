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

class EventsViewModel : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getEvents(year: Int, month: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

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
                            _events.value = eventsList
                        }
                        _isLoading.value = false
                    }
                    .addOnFailureListener {
                        println(it.message)
                        _events.value = emptyList()
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                println(e.message)
                _events.value = emptyList()
                _isLoading.value = false
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