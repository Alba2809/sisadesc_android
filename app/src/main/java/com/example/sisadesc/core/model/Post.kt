package com.example.sisadesc.core.model

import com.google.firebase.Timestamp

data class Post(var id: String? = "", val title: String, val description: String, val date: Timestamp?) {
    constructor() : this("", "", "", null)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "description" to description,
            "date" to date
        )
    }
}