package com.example.sisadesc.core

sealed class Roles(val name: String, val displayName: String){
    object Admin: Roles("admin", "Administrador")
    object Teacher: Roles("teacher", "Profesor")
}