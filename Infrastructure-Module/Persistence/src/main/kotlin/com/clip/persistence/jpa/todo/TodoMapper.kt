package com.clip.persistence.jpa.todo

import com.clip.domain.common.DomainId
import com.clip.domain.todo.entity.Todo
import com.clip.persistence.jpa.todo.entity.TodoEntity

object TodoMapper {
    fun toTodoEntity(todo: Todo): TodoEntity =
        TodoEntity(
            id = todo.id.value,
            userId = todo.userId.value,
            tagId = todo.tagId?.value,
            raw = todo.raw,
            title = todo.title,
            isImportant = todo.isImportant,
            isLife = todo.isLife,
            difficulty = todo.difficulty,
            estimatedTime = todo.estimatedTime,
            deadline = todo.deadline,
            isCompleted = todo.isCompleted
        )

    fun toTodo(todoEntity: TodoEntity): Todo =
        Todo(
            id = DomainId(todoEntity.id),
            userId = DomainId(todoEntity.userId),
            tagId = todoEntity.tagId?.let { DomainId(it) },
            raw = todoEntity.raw,
            title = todoEntity.title,
            isImportant = todoEntity.isImportant,
            isLife = todoEntity.isLife,
            difficulty = todoEntity.difficulty,
            estimatedTime = todoEntity.estimatedTime,
            deadline = todoEntity.deadline,
            isCompleted = todoEntity.isCompleted
        )
}