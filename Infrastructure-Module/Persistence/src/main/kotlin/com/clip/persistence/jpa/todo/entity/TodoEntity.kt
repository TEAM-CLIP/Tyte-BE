package com.clip.persistence.jpa.todo.entity

import com.clip.persistence.jpa.common.BaseEntity
import com.clip.persistence.jpa.common.EntityStatus
import com.clip.persistence.jpa.tag.entity.TagEntity
import com.clip.persistence.jpa.user.entity.UserEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "todo")
class TodoEntity(
    id: String,
    userId: String,
    tagId: String?,
    raw: String,
    title: String,
    isImportant: Boolean,
    isLife: Boolean,
    difficulty: Int,
    estimatedTime: Int,
    deadline: LocalDate,
    isCompleted: Boolean,
) : BaseEntity(id) {
    @Column(nullable = false)
    val userId: String = userId

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    lateinit var user: UserEntity

    @Column
    var tagId: String? = tagId

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId", insertable = false, updatable = false)
    lateinit var tag: TagEntity

    @Column(nullable = false)
    val raw: String = raw

    @Column(nullable = false)
    var title: String = title

    @Column(nullable = false)
    var isImportant: Boolean = isImportant

    @Column(nullable = false)
    var isLife: Boolean = isLife

    @Column(nullable = false)
    var difficulty: Int = difficulty

    @Column(nullable = false)
    var estimatedTime: Int = estimatedTime

    @Column(nullable = false)
    var deadline: LocalDate = deadline

    @Column(nullable = false)
    var isCompleted: Boolean = isCompleted


    @Enumerated(EnumType.STRING)
    @Column(
        name = "todo_status",
        nullable = false,
        columnDefinition = "varchar(20)",
    )
    var todoStatus: EntityStatus = EntityStatus.ACTIVE
}