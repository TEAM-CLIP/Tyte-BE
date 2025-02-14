package com.clip.bootstrap.todo.dto

import java.time.LocalDate

data class CreateTodoRequest(
    val todoText : String,
    val selectedDate : LocalDate
)
