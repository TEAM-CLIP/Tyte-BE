package com.clip.persistence.jpa.tag.entity

import com.clip.persistence.jpa.common.AggregateRoot
import com.clip.persistence.jpa.user.entity.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "tag")
class TagEntity(
    id: String,
    userId: String,
    name: String,
    color: String,
) : AggregateRoot<TagEntity>(id) {

    @Column(nullable = false)
    val name: String = name

    @Column(nullable = false)
    val color: String = color

    @Column(nullable = false)
    val userId: String = userId

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    lateinit var user: UserEntity
}
