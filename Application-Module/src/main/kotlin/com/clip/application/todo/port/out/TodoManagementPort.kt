package com.clip.application.todo.port.out

import com.clip.domain.common.DomainId
import com.clip.domain.todo.entity.Todo
import java.time.LocalDate

interface TodoManagementPort {
    fun saveTodos(todos: List<Todo>) : List<Todo>
    fun getTodoNotNull(todoId: DomainId, userId: DomainId) : Todo
    fun updateTodo(todo: Todo) : Todo
    fun deleteTodo(todo: Todo)
    fun getAllTodoBy(userId: DomainId, selectedDate: LocalDate) : List<Todo>
}