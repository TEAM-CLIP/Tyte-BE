package com.clip.persistence.jpa.todo.repository

import com.clip.persistence.jpa.common.EntityStatus
import com.clip.persistence.jpa.todo.entity.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface TodoJpaRepository : JpaRepository<TodoEntity, String>{
    @Query(
        """
        SELECT t
        FROM TodoEntity t
        WHERE t.id = :todoId
        AND t.userId = :userId
        AND t.todoStatus = :status
    """
    )
    fun findById(todoId: String, userId: String, status: EntityStatus): TodoEntity?

    @Modifying
    @Query(
        """
        UPDATE TodoEntity t
        SET t.todoStatus = :status,
            t.updatedAt = CURRENT_TIMESTAMP
        WHERE t.id = :todoId
        AND t.userId = :userId
    """
    )
    fun updateTodoEntityStatusByIdAndUserId(todoId: String, userId: String, status: EntityStatus)

    @Query(
        """
        SELECT t
        FROM TodoEntity t
        WHERE t.userId = :userId
        AND t.deadline = :deadline
        AND t.todoStatus = :status
    """
    )
    fun findAllByUserIdAndDeadlineAndTodoStatus(userId: String, deadline: LocalDate, status: EntityStatus): List<TodoEntity>
}

fun TodoJpaRepository.findActiveTodoByIdAndUserId(todoId: String, userId: String): TodoEntity?
= findById(todoId, userId, EntityStatus.ACTIVE)

fun TodoJpaRepository.deleteByIdAndUserId(todoId: String, userId: String){
    updateTodoEntityStatusByIdAndUserId(todoId, userId, EntityStatus.DELETED)
}

fun TodoJpaRepository.findAllActiveTodoByUserIdAndDeadline(userId: String, deadline: LocalDate): List<TodoEntity>
= findAllByUserIdAndDeadlineAndTodoStatus(userId, deadline, EntityStatus.ACTIVE)
