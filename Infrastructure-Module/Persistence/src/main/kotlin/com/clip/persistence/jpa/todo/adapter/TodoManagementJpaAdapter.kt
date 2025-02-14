package com.clip.persistence.jpa.todo.adapter

import com.clip.application.todo.exception.TodoException
import com.clip.application.todo.port.out.TodoManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.todo.entity.Todo
import com.clip.persistence.jpa.todo.TodoMapper
import com.clip.persistence.jpa.todo.repository.TodoJpaRepository
import com.clip.persistence.jpa.todo.repository.deleteByIdAndUserId
import com.clip.persistence.jpa.todo.repository.findActiveTodoByIdAndUserId
import com.clip.persistence.jpa.todo.repository.findAllActiveTodoByUserIdAndDeadline
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class TodoManagementJpaAdapter(
    private val todoJpaRepository: TodoJpaRepository
): TodoManagementPort {
    override fun saveTodos(todos: List<Todo>): List<Todo> {
        val todoEntities = todos.map { TodoMapper.toTodoEntity(it) }
        todoJpaRepository.saveAll(todoEntities)
        return todos
    }

    override fun getTodoNotNull(todoId: DomainId, userId: DomainId): Todo =
        todoJpaRepository.findActiveTodoByIdAndUserId(todoId.value, userId.value)?.let {
            TodoMapper.toTodo(it)
        } ?: throw TodoException.TodoNotFoundException()

    override fun updateTodo(todo: Todo): Todo {
        val todoEntity = TodoMapper.toTodoEntity(todo)
        todoEntity.update()
        todoJpaRepository.save(todoEntity)
        return todo
    }

    override fun deleteTodo(todo: Todo) {
        todoJpaRepository.deleteByIdAndUserId(
            todoId = todo.id.value,
            userId = todo.userId.value
        )
    }

    override fun getAllTodoBy(userId: DomainId, selectedDate: LocalDate): List<Todo> =
        todoJpaRepository.findAllActiveTodoByUserIdAndDeadline(userId.value, selectedDate)
            .map { TodoMapper.toTodo(it) }

}