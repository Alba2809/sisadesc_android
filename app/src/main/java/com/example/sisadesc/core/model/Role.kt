package com.example.sisadesc.core.model

data class Role(
    val id: String,
    val name: String,
    val displayName: String
) {
    constructor() : this("", "", "")

    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "id" to this.id,
            "name" to this.name,
            "displayName" to this.displayName
        )
    }
}