package com.example.sisadesc.core.model

import com.google.firebase.firestore.DocumentReference

//user with all his data (address needs to be added)
data class UserDetailed(
    val id: String?,
    val userId: String,
    val name: String,
    val avatarUrl: String,
    val role: DocumentReference?,
    var roleData: Role?,
    var email: String
){
    constructor(): this("", "", "", "", null, null, "")
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "userId" to this.userId,
            "name" to this.name,
            "avatarUrl" to this.avatarUrl,
            "role" to this.role,
            "roleData" to this.roleData?.toMap(),
            "email" to this.email
        )
    }
}