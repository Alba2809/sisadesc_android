package com.example.sisadesc.core.model

data class UserLogged(
    val id: String?,
    val userId: String,
    val name: String,
    val avatarUrl: String,
){
    constructor(): this("", "", "", "")
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "userId" to this.userId,
            "name" to this.name,
            "avatarUrl" to this.avatarUrl
        )
    }
}