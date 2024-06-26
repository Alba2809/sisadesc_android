package com.example.sisadesc.core.data

sealed class Roles(val name: String, val displayName: String){
    object Admin: Roles("admin", "Administrador")
    object Teacher: Roles("teacher", "Profesor")
}