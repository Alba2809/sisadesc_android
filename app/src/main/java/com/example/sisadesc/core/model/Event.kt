package com.example.sisadesc.core.model

import com.google.firebase.Timestamp

data class Event(val id: String, val description: String, val startTime: Timestamp, val endTime: Timestamp) {
    constructor() : this("", "", Timestamp.now(), Timestamp.now())
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "description" to description,
            "startTime" to startTime,
            "endTime" to endTime
        )
    }
}