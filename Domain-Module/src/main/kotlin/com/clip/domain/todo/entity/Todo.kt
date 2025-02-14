package com.clip.domain.todo.entity

import com.clip.domain.common.AggregateRoot
import com.clip.domain.common.DomainId
import java.time.LocalDate

class Todo(
    id: DomainId = DomainId.generate(),
    val userId: DomainId,
    var tagId: DomainId?,
    val raw: String,
    var title: String,
    var isImportant: Boolean,
    var isLife: Boolean,
    var difficulty: Int,
    var estimatedTime: Int,
    var deadline: LocalDate,
    var isCompleted: Boolean,
): AggregateRoot<Todo>(id) {
    companion object {
        fun create(
            userId: DomainId,
            tagId: DomainId?,
            raw: String,
            title: String,
            isImportant: Boolean,
            isLife: Boolean,
            difficulty: Int,
            estimatedTime: Int,
            deadline: LocalDate,
            isCompleted: Boolean,
        ): Todo {
            return Todo(
                userId = userId,
                tagId = tagId,
                raw = raw,
                title = title,
                isImportant = isImportant,
                isLife = isLife,
                difficulty = difficulty,
                estimatedTime = estimatedTime,
                deadline = deadline,
                isCompleted = isCompleted,
            )
        }
    }

    fun toggleCompletion() {
        isCompleted = !isCompleted
    }

    fun update(
        title: String,
        isImportant: Boolean,
        isLife: Boolean,
        difficulty: Int,
        estimatedTime: Int,
        deadLine: String,
        tagId: String
    ) {
        this.title = title
        this.isImportant = isImportant
        this.isLife = isLife
        this.difficulty = difficulty
        this.estimatedTime = estimatedTime
        this.deadline = LocalDate.parse(deadLine)
        this.tagId = DomainId(tagId)
    }
}